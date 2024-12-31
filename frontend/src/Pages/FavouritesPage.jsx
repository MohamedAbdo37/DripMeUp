import { useEffect, useState } from "react";
import Modal from 'react-modal';
import '../style.css'
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import binIcon from "../assets/bin.png";
import emptyFavIcon from "../assets/emptyFav.png";

const FavouritesPage = ()=>{

     const [productsInCart, setProductsInCart] = useState([
            //     {
            //         color: "", 
            //         description:"",
            //         images:[""],
            //         length:"", 
            //         price:"",
            //         productId: 0,
            //         size:"",
            //         state:"",
            //         stock:0,
            //         variantId:0,
            //         weight:""
            //     }
        ]);
        const navigate = useNavigate();
    
        useEffect(()=>{
            getProductsInFav();
        }, []);
    
        const notifySuccess = (message) => {
            toast.success(message);
        };
    
        const notifyFailier = (message) => {
            toast.error(message);
        };
    
        const getProductsInFav = async()=>{
            await fetch(`http://localhost:8081/api/favorites/get`, {
                method:"GET",
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
                }
            })
            .then (response=>response.status==200||response.status==201?(()=>{return response.json()})(): (()=>{throw Error("Error fetching cart products")})())
            .then(cartProductsData=>{console.log(cartProductsData);setProductsInCart(cartProductsData)})
            .catch(e=>console.error(e));
        }
    
       
    
        const deleteFromFav = async(variantId)=>{
            await fetch (`http://localhost:8081/api/favorites/delete/${variantId}`, {
                method:"DELETE",
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
                }
            })
            .then (response=>response.status==200||response.status==201?(()=>notifySuccess("Product deleted succeffully"))(): (()=>{throw Error("Error deleting product from cart")})())
            .catch(e=>{console.error(e); notifyFailier("Failed to delete element from cart")});
            location.reload();
        }
    
        const clearFav = async ()=>{
            await fetch(`http://localhost:8081/api/favorites/clear`,{
                method:"DELETE",
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
                }
            })
            .then (response=>response.status==200||response.status==201?(()=>notifySuccess("Cart deleted Succeffully"))(): (()=>{throw Error("Error deleting cart")})())
            .catch(e=>{console.error(e); notifyFailier("Failed to delete cart")});
            location.reload();
        }
        return(
        <>
            <div style={{width:"100%"}}>
                {productsInCart.length!=0 && productsInCart.map((product, key)=>(
                    <div className="productCard" key={key}>
                        <div style={{width:"90%", marginRight:"1.5rem"}} onClick={()=>{navigate(`/userSession/product/other/${product.productId}/${product.variantId}`)}}>
                            <img src={product.images[0]} alt="VariantImage" style={{marginRight:"1rem"}}/>
                            <div style={{fontSize:"1.5rem"}}>
                                <p style={{margin:"0"}}>{product.description}</p>
                                <p style={{margin:"0"}}>Color: {product.color}</p>
                                <p style={{margin:"0"}}>Size: {product.size}</p>
                                <p style={{margin:"0"}}>Price: {product.price} LE</p>
                                <p style={{margin:"0"}}>{product.state}</p>
                            </div>
                        </div>
                        <img src={binIcon} className="backButton" alt="binImage" onClick={()=>deleteFromFav(product.element.variantId)}/>
                    </div>
                ))}
            </div>
            {productsInCart.length!=0 &&<center>
                <img onClick={clearFav} title="Empty the whole favourites" style={{width:"5rem", height:"4rem"}} src={emptyFavIcon} alt="clearFavIcon" className="backButton"/>
            </center>}
            {productsInCart.length==0 && <center>
            <img src={emptyFavIcon} alt="emptyCartImage"/> 
            <h1>Looks like you have not added anything to you favourites. Go
            ahead & explore top categories.</h1>
            </center>}
        </>
       
    );
};
export default FavouritesPage;
