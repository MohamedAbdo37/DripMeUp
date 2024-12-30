import { useEffect, useState } from "react";
import Modal from 'react-modal';
import '../style.css'
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
const CartPage = ()=>{
    const [productsInCart, setProductsInCart] = useState([
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
    const [isOrdering, setIsOrdering] = useState(false);

    useEffect(()=>{
        getProductsInCart();
    }, []);

    const notifySuccess = (message) => {
        toast.success(message);
    };

    const notifyFailier = (message) => {
        toast.error(message);
    };

    const getProductsInCart = async()=>{
        await fetch(`http://localhost:8081/getProductsInCart`, {
            method:"GET",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then (response=>response.status==200||response.status==201?(()=>{return response.json()})(): (()=>{throw Error("Error fetching cart products")})())
        .then(cartProductsData=>setProductsInCart(cartProductsData))
        .catch(e=>console.error(e));
    }

    const buy = async(e)=>{
        e.preventDefault();
        console.log("lkdfldskjf")
        await fetch(`http://localhost:8081/addOrder`, {
            method:"POST",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            },
            body:{
                products: JSON.stringify(productsInCart),
                ...formVariables
            }
        })
        .then (response=>response.status==200||response.status==201?(()=>{return response.json()})(): (()=>{throw Error("Error fetching cart products")})())
        .then(cartProductsData=>{setProductsInCart(cartProductsData);notifySuccess("Order created succeffully")})
        .catch(e=>{console.error(e); notifyFailier("Failed to create your order")});
    }

    const handleChange = (e)=>{
        const {name, value} = e.target;
        setFormVariables({...formVariables, [name]: value});
    }
    return(
    <>
        <div style={{width:"100%"}}>
        <center><h1>Cart</h1></center>
            {productsInCart.map((product, key)=>(
                <div className="productCard" key={key} onClick={()=>{navigate(`/userSession/product/cart/${product.productID}`)}}>
                    <img src={product.variants[0].images[0]} alt="VariantImage" style={{marginRight:"1rem"}}/>
                    <div style={{fontSize:"1.5rem"}}>
                        <p style={{margin:"0"}}>{product.description}</p>
                        <p style={{margin:"0"}}>{product.state}</p>
                    </div>
                </div>
            ))}
        </div>
        <center><button onClick={()=>setIsOrdering(true)} className="backButton">Create Order</button></center>
        <Modal
        isOpen={isOrdering}
        onRequestClose={()=>setIsOrdering(false)}
        style={{content:{width:"50%", height:"50%",top: "50%",
            left: "50%",
            right: "auto", 
            bottom: "auto", 
            marginRight: "-50%",
            transform: "translate(-50%, -50%)"}}}
        >
        <form onSubmit={buy}>
        <label htmlFor="paymentMethod" style={{marginRight:"0.5rem"}}>Payment Method</label>
        <select name="paymentMethod" onChange={handleChange} required>
            <option value="" disabled selected>Select payment method</option>
            <option value="VISA">VISA Card</option>
            <option value="CASH">Cash</option>
        </select>
        {formVariables.paymentMethod && formVariables.paymentMethod == "VISA" && <>
        <input type="text"
        name="cardNumber"
        maxLength="16"
        value={formVariables.cardNumber}
        onChange={handleChange}
        placeholder="Card Number"
        pattern="^\d{16}$"
        title="Write the 16 numbers on your credit card"
        required
      />
      <input type="text"
        name="cardHolder"
        value={formVariables.cardHolder}
        onChange={handleChange}
        placeholder="Card Holder"
        pattern="^[a-zA-Z\s]+$"
        title="Write the name of the card holder"
        required
      />
      <input type="text"
        name="expirationDate"
        value={formVariables.expirationDate}
        onChange={handleChange}
        placeholder="Expiration Date MM/YY"
        pattern="^(0[1-9]|1[0-2])\/\d{2}$"
        title="Write the expiration date of the credit card in from of MM/YY"
        required
      />
      <input type="text"
        name="CVV"
        maxLength="3"
        value={formVariables.CVV}
        onChange={handleChange}
        placeholder="CVV"
        pattern="\d{3}$"
        title="Write the CVV three numbers on your credit card"
        required
      /></>
        }
        <center>
            <button type="submit" className="backButton">Buy</button>
            <button onClick={()=>setIsOrdering(false)} style={{marginLeft:"1rem"}} className="backButton">Cancel</button>
        </center>
        </form>
        </Modal>
    </>
    );
};
export default CartPage;