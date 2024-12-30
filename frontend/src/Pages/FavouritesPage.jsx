import { useEffect, useState } from "react";
import Modal from 'react-modal';
import '../style.css'
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const FavouritesPage = ()=>{

    const [productsInFav, setProductsInFav] = useState([
        {
            "productID": 1,
            "productImage": "https://example.com/image1.jpg",
            "price": "$19.99",
            "description": "Product 1 description",
            "state": "ON_SALE",
            "variants":[
                {
                "images":["C:\\Users\\KimoStore\\Desktop\\s-l960.png"]
                }
            ]
          },
          {
            "productID": 2,
            "productImage": "https://example.com/image2.jpg",
            "price": "$29.99",
            "description": "Product 2 description",
            "state": "ON_SALE",
            "variants":[
                {
                "images":["C:\\Users\\KimoStore\\Desktop\\s-l1600.png"]
                }
            ]
          }
    ]);
    const navigate = useNavigate();
    const [formVariables, setFormVariables] = useState({});

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
        await fetch(`http://localhost:8081/getProductsInFav`, {
            method:"GET",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then (response=>response.status==200||response.status==201?(()=>{return response.json()})(): (()=>{throw Error("Error fetching cart products")})())
        .then(favProductsData=>setProductsInFav(favProductsData))
        .catch(e=>console.error(e));
    }


    return(
        <div style={{width:"100%"}}>
            <center><h1>Favourites</h1></center>
            {productsInFav.map((product, key)=>(
                <div className="productCard" key={key} onClick={()=>{navigate(`/userSession/product/user/${product.productID}`)}}>
                    <img src={product.variants[0].images[0]} alt="VariantImage" style={{marginRight:"1rem"}}/>
                    <div style={{fontSize:"1.5rem"}}>
                        <p style={{margin:"0"}}>{product.description}</p>
                        <p style={{margin:"0"}}>{product.state}</p>
                    </div>
                </div>
            ))}
        </div>
       
    );
};
export default FavouritesPage;
