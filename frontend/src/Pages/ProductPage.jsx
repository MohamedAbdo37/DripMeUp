import { useNavigate, useParams } from "react-router-dom";
import unknownPhoto from '../assets/pic.png'; // Adjust the path as necessary
import FeedbackBox from '../Components/FeedbackBox';
import favouriteImage from '../assets/favourite.png';
import filledStar from '../assets/filledStar.png';
import star from '../assets/star.png';
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import '../style.css';
import { b, body } from "framer-motion/client";
import addToCartIcon from "../assets/addToCart.png";
import AddProductForm from "../Components/AddProductForm";
import Modal from 'react-modal';



const ProductPage = () =>{
    const { productID, person, currentSelectedVariantId } = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState({ dateOfCreation:"", variants: [{variantID: 0, color: "", weight: null, length: null, size: null, stock: null, sold: null, state: null, price: null, discount: null, images: [""]}]});
    const [currentVariant, setCurrentVariant] = useState(0); 
    const [feedbacks, setFeedbacks] = useState([]);
    const [newFeedback, setNewFeedback] = useState("");
    const [isEditing, setIsEditing] = useState(false);




    useEffect(()=>{
        if (!productID) {
            console.error("Product ID is undefined");
            return;
        }
        fetchFeedbackForProduct(productID);
        getProdect();


    },[productID]);

    const getProdect = async()=>{
        await fetch(`http://localhost:8081/api/1000/shop/product?productID=${productID}`,{
            method:'GET',
            headers:{
                'Content-Type': 'application/json',
                 'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
                }
        })
        .then(responde=>responde.status==200 || responde.status==201 ? (()=>{return responde.json()})() : (()=>{throw Error("Error fetching products")})())
        .then(data=>{setProduct(()=>data)})
        .catch(e=>console.log(e));
    }

    const fetchFeedbackForProduct = async (productID) => {
        try {
          const response = await fetch(`http://localhost:8081/api/feedback/product/${productID}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("drip_me_up_jwt")}`,
            },
          });
      
          if (!response.ok) throw new Error("Failed to fetch feedback");
      
          const data = await response.json();
          setFeedbacks(data); // Assuming the response is a list of feedback

        } catch (error) {
          console.error("Error fetching feedback:", error);
        }
      };
      

      const handleAddFeedback = async () => {
        if (!newFeedback.trim()) return; // Don't submit if feedback is empty
    
        try {
            // Decode the JWT to get the user info
            const token = localStorage.getItem('drip_me_up_jwt');
    
            const decodedToken = JSON.parse(atob(token.split('.')[1]));
            const userId = decodedToken.userId || decodedToken.id || decodedToken.sub; // Adjust if the userId is stored under a different key
            if (!userId) {
                throw new Error("User ID not found in token");
            }
            

            const response = await fetch(`http://localhost:8081/api/feedback`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    feedback: newFeedback,                    
                    productId: productID,
                    userId: userId // Use the decoded userId
                }),
                
                
                
            });
    
            if (response.ok) {
                const feedbackData = await response.json();
                setFeedbacks([...feedbacks, feedbackData]); // Add new feedback to the list
                setNewFeedback(""); // Clear input field
                notifySuccess("Feedback added successfully!");
            } else {
                notifyFailier("Failed to add feedback.");
            }
        } catch (error) {
            console.error("Error adding feedback:", error);
            notifyFailier("An error occurred while adding your feedback.");
        }
    };
      
    const notifySuccess = (message) => {
        toast.success(message);

    };

    const notifyFailier = (message) => {
        toast.error(message);
    };
    const selectVariant = (index, event)=>{
        let target = event.target;
        if(event.target.className == 'variantCardChild') 
            target = target.parentElement;
        const children =  target.parentElement.children;
        Array.from(children).forEach((child) => {
            child.className = "variantCard";
        });
        target.className = "selectedVariantCard";
        setCurrentVariant(index);
    }
   
    const addToCart = async()=>{
        let numberOfVariants = prompt("Enter number of desired variants");
        if (numberOfVariants == null) return
        if (numberOfVariants < 1){
            notifyFailier("Entered amount must be 1 or more");
            return
        }
        await fetch(`http://localhost:8081/api/cart/add/${product.variants[currentVariant].variantID}?amount=${numberOfVariants}`,{
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then(response=>response.status == 200 || response.status == 201? notifySuccess("Added to cart succeffully"): (()=>{
            if(response.status == 409){
                notifyFailier("Entered amount is out of stock");
            }
            else{
                notifyFailier("Failed to add to cart");
            }
        })())
        .catch(e=>{console.log(e);notifyFailier("Failed to add to cart");});
    }
    

    const deleteProduct = async(deleteFlag = false)=>{
        await fetch(`http://localhost:8081/products/${productID}`,{
            method: 'DELETE',
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then(response=>response.status == 200 || response.status == 201? (()=>{if (deleteFlag) notifySuccess("Product deleted successfully"); navigate(location.pathname.split('/').slice(0, 2).join('/'))})(): (()=>{
            if (deleteFlag)
                notifyFailier("Failed to delete the product");
            else 
                throw Error("Error deleting the product");
        })())
        .catch(e=>{
            if (deleteFlag){
                console.log(e);
                notifyFailier("Failed to delete the product")
            }else{
                throw e;
            }
        });
    }
    const addToFavourites = async()=>{
        await fetch(`http://localhost:8081/api/favorites/add/${product.variants[currentVariant].variantID}`, {
            method:"POST",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then(response=>response.status == 200 || response.status == 201? notifySuccess("Product added to favourits successfully"): notifyFailier("Failed to add to favourits"))
        .catch(e=>{console.log(e);notifyFailier("Failed to add to favourits")});
    }
    const addFeedback = ()=>{
        
    }
    return (
        <div style={{fontSize: "1.5rem"}}>
            <div className="productImg">
                <div className="Img">
                    <img src={product.variants[currentVariant].images[0] || unknownPhoto} alt="ProductPhoto" style={{width:"25rem", height: "25rem"}}/>
                </div>
                <div className="share-favouriteButtons">
                    {person!="admin"&&<img src={favouriteImage} alt="FavouritePhoto" onClick={addToFavourites}/>}
                </div>
            </div>
            <div className="controller">
                <div className="controllerLeft">
                    <div>
                        <h6 style={{margin: "0"}}>{(product.dateOfCreation).split("T")[0]}</h6>
                        <p style={{fontSize: "3rem", margin: "0"}}>{product.description}</p><br/>
                        <p style={{fontSize: "2rem", margin: "0"}}>{product.variants[currentVariant].price-(product.variants[currentVariant].price * product.variants[currentVariant].discount)} LE</p>
                    </div>
                    {/* {product.variants[currentVariant].state == 'ON_SALE' &&
                    <div className="saleData">
                        <p style={{color: 'green', margin: "0", marginRight:"1rem"}}>Available</p>
                        <p style={{textDecoration: 'line-through', margin: "0", marginRight:"1rem"}}>old: {product.variants[currentVariant].price} LE</p>
                        <div className="sale">
                            SALE {product.variants[currentVariant].discount * 100} %
                        </div>
                    </div>} */}
                    {product.variants[currentVariant].state == 'OUT_OF_STOCK' &&
                    <p style={{color:'#f2e6e8', margin: '0'}}>Out of stock at the moment</p>
                    }
                    {product.variants[currentVariant].state == 'DISCONTINUED' &&
                    <p style={{color: 'green', margin: "0", marginRight:"1rem"}}>Available</p>
                    }
                </div>
                <div className="controllerRight">
                    <div className="controllerButtons">
                        {person!="admin"&&<img src={addToCartIcon} onClick={addToCart} style={{cursor:"pointer", width:"3.5rem", height:"3.5rem"}}/>}
                    </div>
                    {/* {person == 'admin' &&
                        <div className="controllerButtons">
                            <button onClick={()=>setIsEditing(true)}>Edit</button>
                            <button onClick={()=>deleteProduct(true)}>Delete</button>
                        </div>
                    } */}
                    <div className="controllerRightDescription">
                        {/* <p>Category: {product.category}</p> */}
                        <p>No. of remaining items: {product.variants[currentVariant].stock}</p>
                    </div>
                </div>
            </div>
            <div className="variants">
                {product.variants.map((variant, index)=>(
                    <div className={person=="other"&&variant.variantID == currentSelectedVariantId?"selectedVariantCard":(()=>{if(index==0 && person!="other") return "selectedVariantCard"; else return "variantCard";})()} key={index} onClick={(event)=>selectVariant(index, event)}>
                        <img className="variantCardChild" style={{width:"3rem", height:"3rem"}} src={variant.images[0] || unknownPhoto} alt="variantImage"/>
                        <h5 className="variantCardChild">{variant.color} | {variant.size}</h5>
                    </div>
                ))}
            </div>
            <table className="styled-table">
                <tbody>
                    <tr>
                        <td style={{width: "0", fontWeight:"bold"}}>Color</td>
                        <td style={{}}>{product.variants[currentVariant].color}</td>
                        <td style={{width: "0", fontWeight:"bold"}}>Size</td>
                        <td>{product.variants[currentVariant].size}</td>
                    </tr>
                    <tr>
                        <td style={{fontWeight:"bold"}}>Weight</td>
                        <td style={{}}>{product.variants[currentVariant].weight}</td>
                        <td style={{fontWeight:"bold"}}>Length</td>
                        <td>{product.variants[currentVariant].length}</td>
                    </tr>
                </tbody>
            </table>
            <div className="feedbacks">
                {feedbacks.map((feedback, i)=>(
                    <FeedbackBox key={i} feedback={feedback}/>
                ))}
            </div>

            {person === 'user' && (
                <div className="addFeedback">
                    <textarea 
                        value={newFeedback} 
                        onChange={(e) => setNewFeedback(e.target.value)} 
                        placeholder="Add your feedback here..."
                        rows="4" 
                        style={{width: "100%", marginBottom: "10px", fontSize:"2rem"}} 
                    />
                    <button className="backButton" onClick={handleAddFeedback}>

                        Add Feedback
                    </button>
                </div>
            )}

            <Modal 
                isOpen={isEditing}
                onRequestClose={()=>setIsEditing(false)}
                style={{content:{background:"white"}}}
            >
                <button className="backButton" onClick={()=>setIsEditing(false)}>X</button>
                <AddProductForm loadedProductId={productID} deleteFunction={deleteProduct}/>
            </Modal>

            
        </div>
        
    );
};
export default ProductPage;