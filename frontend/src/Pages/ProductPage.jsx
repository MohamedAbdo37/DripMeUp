import { useOutletContext, useParams } from "react-router-dom";
import unknownPhoto from '../assets/pic.png'; // Adjust the path as necessary
import FeedbackBox from '../Components/FeedbackBox';
import favouriteImage from '../assets/favourite.png';
import filledStar from '../assets/filledStar.png';
import star from '../assets/star.png';
import shareImage from '../assets/share.png';
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

const ProductPage = () =>{
    const {id} = useParams();
    const [product, setProduct] = useState();
    // useEffect(async ()=>{
    //     const getProducts = await fetch(`http://localhost:8081/api/5/getProduct`,{
    //         method:'GET',
    //         headers:{
    //             'Content-Type': 'application/json',
    //             'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
    //             }
    //     })
    //     .then(responde=>responde.status==200 || responde.status==201 ? (()=>{return responde.json()})() : (()=>{throw Error("Error fetching products")})())
    //     .then(data=>setProduct(data))
    //     .catch(e=>console.log(e));
    // },[]);

    const notifyAddToCart = () => {
        toast.success(`Product added to cart successfully`);
    };

    const notifyFailAddToCart = () => {
        toast.error(`Error adding to cart`);
    };
    const person = 'user';
    setProduct({
        description:"Description goes here",
        rate: 2,
        photo: null,
        name: "Name",
        price: 100,
        salePercentage: 0.5,
        category: "summerSherts",
        amountInStock: 5,
        color: "red",
        size: "XL",
        weight: "50 gm",
        length: "",
        feedbacks: [
            {
                user: {
                    name: "Ibrahim",
                    photo: null
                },
                feedback: "Good",
                time: "11AM 25/12/2020"
            },
            {
                user: {
                    name: "Mohamed",
                    photo: null
                },
                feedback: "Good",
                time: "11AM 25/12/2021"
            }
        ]
    });
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
                    <p>{product.description}</p>
                    <div className="rating">
                        {Array.from({length: product.rate}, (_, i)=>(<img src={filledStar} alt="yellowStar" className="yellowStar"/>))}
                        {Array.from({length: 5-product.rate}, (_, i)=>(<img src={star} alt="emptyStar" className="emptyStar"/>))}
                        {product.rate}/5
                    </div>
                </div>
                <div className="Img">
                    <img src={product.photo || unknownPhoto} alt="ProductPhoto" style={{width:"15rem", height: "15rem"}}/>
                </div>
                <div className="share-favouriteButtons">
                    <img src={favouriteImage} alt="FavouritePhoto" onClick={addToFavourites}/>
                    <img src={shareImage} alt="FavouritePhoto" onClick={share}/>
                </div>
            </div>
            <div className="controller">
                <div className="controllerLeft">
                    <div>
                        <p style={{fontSize: "3rem", margin: "0"}}>{product.name}</p>
                        <p style={{fontSize: "2rem", margin: "0"}}>{product.price * product.salePercentage} LE</p>
                    </div>
                    <div className="saleData">
                        <p style={{textDecoration: 'line-through', margin: "0", marginRight:"1rem"}}>old: {product.price} LE</p>
                        <div className="sale">
                            SALE {product.salePercentage * 100} %
                        </div>
                    </div>
                </div>
                <div className="controllerRight">
                    {person == 'user' &&
                        <div className="controllerButtons">
                            <button onClick={buy}>Buy</button>
                            <button onClick={addToCart} style={{backgroundColor: "#2feb7d"}}>Add to Cart</button>
                        </div>
                    }
                    {person == 'admin' &&
                        <div className="controllerButtons">
                            <button onClick={edit}>Edit</button>
                            <button onClick={deleteProduct}>Delete</button>
                        </div>
                    }
                    <div className="controllerRightDescription">
                        <p>Category: {product.category}</p>
                        <p>No. of remaining items: {product.amountInStock}</p>
                    </div>
                </div>
            </div>
            <table className="styled-table">
                <tbody>
                    <tr>
                        <td style={{width: "0", fontWeight:"bold"}}>Color</td>
                        <td style={{}}>{product.color}</td>
                        <td style={{width: "0", fontWeight:"bold"}}>Size</td>
                        <td>{product.size}</td>
                    </tr>
                    <tr>
                        <td style={{fontWeight:"bold"}}>Weight</td>
                        <td style={{}}>{product.weight}</td>
                        <td style={{fontWeight:"bold"}}>Length</td>
                        <td>{product.length}</td>
                    </tr>
                </tbody>
            </table>
            <div className="feedbacks">
                {product.feedbacks.map((feedback, i)=>(
                    <FeedbackBox key={i} feedback={feedback}/>
                ))}
            </div>
        </div>
    );
};
export default ProductPage;