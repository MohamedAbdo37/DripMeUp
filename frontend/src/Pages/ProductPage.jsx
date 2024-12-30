import { useLocation, useParams } from "react-router-dom";
import unknownPhoto from '../assets/pic.png'; // Adjust the path as necessary
import FeedbackBox from '../Components/FeedbackBox';
import favouriteImage from '../assets/favourite.png';
import filledStar from '../assets/filledStar.png';
import star from '../assets/star.png';
import shareImage from '../assets/share.png';
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import '../style.css';


const ProductPage = () =>{
    const { productID, person } = useParams();
    const [product, setProduct] = useState({productImage: "", dateOfCreation:"", variants: [{variantID: null, color: "", weight: null, length: null, size: null, stock: null, sold: null, state: null, price: null, discount: null, variantImage: ""}]});
    const [currentVariant, setCurrentVariant] = useState(0); 
    const [feedbacks, setFeedbacks] = useState([]);
    const [newFeedback, setNewFeedback] = useState("");



    useEffect(()=>{
        if (!productID) {
            console.error("Product ID is undefined");
            return;
        }
        fetchFeedbackForProduct(productID);

    },[productID]);

    const getProdect = async()=>{
        const productsFetch = await fetch(`http://localhost:8081/api/1000/shop/product?productID=${productID}`,{
            method:'GET',
            headers:{
                'Content-Type': 'application/json',
                 //'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
                }
        })
        .then(responde=>responde.status==200 || responde.status==201 ? (()=>{return responde.json()})() : (()=>{throw Error("Error fetching products")})())
        .then(data=>{setProduct(()=>data);console.log(data)})
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
          console.log(data);
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
                })
            });
    
            if (response.ok) {
                const feedbackData = await response.json();
                setFeedbacks([...feedbacks, feedbackData]); // Add new feedback to the list
                setNewFeedback(""); // Clear input field
                toast.success("Feedback added successfully!");
            } else {
                toast.error("Failed to add feedback.");
            }
        } catch (error) {
            console.error("Error adding feedback:", error);
            toast.error("An error occurred while adding your feedback.");
        }
    };
      
    const notifyAddToCart = () => {
        toast.success(`Product added to cart successfully`);
    };

    const notifyFailAddToCart = () => {
        toast.error(`Error adding to cart`);
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
    const buy = ()=>{

    }
    const addToCart = async()=>{
        // const addCart = await fetch(`http://localhost:8081/cart/${id}`,{
        //     method: 'POST',
        //     headers:{
        //         'Content-Type': 'application/json',
        //         'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
        //     }
        // })
        // .then(response=>response.status == 200 || response.status == 201? notifyAddToCart(): notifyFailAddToCart())
        // .catch(e=>console.log(e));
    }
    const edit = ()=>{

    }
    const deleteProduct = ()=>{
        // const addCart = await fetch(`http://localhost:8081/products/${id}`,{
        //     method: 'DELETE',
        //     headers:{
        //         'Content-Type': 'application/json',
        //         'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
        //     }
        // })
        // .then(response=>response.status == 200 || response.status == 201? notifyDelete(): notifyFailDelete())
        // .catch(e=>console.log(e));
    }
    const share = ()=>{

    }
    const addToFavourites = ()=>{

    }
    return (
        <div style={{fontSize: "1.5rem"}}>
            <div className="productImg">
                <div className="ratingBox">
                    <div className="rating">
                        {product.numberOfFeedback}
                        {Array.from({length: product.rate}, (_, i)=>(<img src={filledStar} alt="yellowStar" className="yellowStar"/>))}
                        {Array.from({length: 5-product.rate}, (_, i)=>(<img src={star} alt="emptyStar" className="emptyStar"/>))}
                        {product.rate}/5
                    </div>
                </div>
                <div className="Img">
                    <img src={product.productImage || unknownPhoto} alt="ProductPhoto" style={{width:"15rem", height: "15rem"}}/>
                </div>
                <div className="share-favouriteButtons">
                    <img src={favouriteImage} alt="FavouritePhoto" onClick={addToFavourites}/>
                    <img src={shareImage} alt="FavouritePhoto" onClick={share}/>
                </div>
            </div>
            <div className="controller">
                <div className="controllerLeft">
                    <div>
                        <h6 style={{margin: "0"}}>{(product.dateOfCreation).split("T")[0]}</h6>
                        <p style={{fontSize: "3rem", margin: "0"}}>{product.description}</p>
                        <p style={{fontSize: "2rem", margin: "0"}}>{product.variants[currentVariant].price-(product.variants[currentVariant].price * product.variants[currentVariant].discount)} LE</p>
                    </div>
                    {product.variants[currentVariant].state == 'ON_SALE' &&
                    <div className="saleData">
                        <p style={{color: 'green', margin: "0", marginRight:"1rem"}}>Available</p>
                        <p style={{textDecoration: 'line-through', margin: "0", marginRight:"1rem"}}>old: {product.variants[currentVariant].price} LE</p>
                        <div className="sale">
                            SALE {product.variants[currentVariant].discount * 100} %
                        </div>
                    </div>}
                    {product.variants[currentVariant].state == 'OUT_OF_STOCK' &&
                    <p style={{color:'#f2e6e8', margin: '0'}}>Out of stock at the moment</p>
                    }
                    {product.variants[currentVariant].state == 'DISCONTINUED' &&
                    <p style={{color: 'green', margin: "0", marginRight:"1rem"}}>Available</p>
                    }
                </div>
                <div className="controllerRight">
                    {person == 'user' &&
                        <div className="controllerButtons">
                            <button onClick={buy}>Buy</button>
                            <button onClick={addToCart} style={{backgroundColor: "#3cdc66"}}>Add to Cart</button>
                        </div>
                    }
                    {person == 'admin' &&
                        <div className="controllerButtons">
                            <button onClick={edit}>Edit</button>
                            <button onClick={deleteProduct}>Delete</button>
                        </div>
                    }
                    <div className="controllerRightDescription">
                        {/* <p>Category: {product.category}</p> */}
                        <p>No. of remaining items: {product.variants[currentVariant].stock}</p>
                    </div>
                </div>
            </div>
            <div className="variants">
                {product.variants.map((variant, index)=>(
                    <div className="variantCard" key={index} onClick={(event)=>selectVariant(index, event)}>
                        <img className="variantCardChild" src={variant.variantImage || unknownPhoto} alt="variantImage"/>
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
                        style={{width: "100%", marginBottom: "10px"}} 
                    />
                    <button onClick={handleAddFeedback}>
                        Add Feedback
                    </button>
                </div>
            )}
            
        </div>
        
    );
};
export default ProductPage;