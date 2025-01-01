import { useEffect, useState } from "react";
import Modal from 'react-modal';
import '../style.css'
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import binIcon from "../assets/bin.png";
import emptyCartIcon from "../assets/emptyCart.png";
import deleteCartIcon from "../assets/deleteCart.png";
import createOrderIcon from "../assets/createOrder.png";
import logoIcon from "../assets/logo.png";
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "../Components/ObjectToAppear";

const CartPage = ()=>{
    const [productsInCart, setProductsInCart] = useState([
        // {
        //     amount: 0,
        //     element:{
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
        //   }
    ]);
    const navigate = useNavigate();
    const [formVariables, setFormVariables] = useState({cardNumber:"", cardHolder:"", expirationDate:"", cvv:""});
    const [isOrdering, setIsOrdering] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

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
        await fetch(`http://localhost:8081/api/cart/get`, {
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

    const buy = async(e)=>{
        e.preventDefault();
        setIsLoading(()=>true);
        console.log("order: ", formVariables);
        await fetch(`http://localhost:8081/orders/create-order`, {
            method:"POST",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            },
            body:JSON.stringify(formVariables)
        })
        .then (response=>response.status==200||response.status==201?(()=>{setIsLoading(()=>false);notifySuccess("Order created succeffully");location.reload();})(): (()=>{
            setIsLoading(()=>false);
            if (response.status == 409){
                (()=>response.json())().then(conflictData=>{
                    notifyFailier(`These products out of stock [${conflictData.forEach((id)=>{id})}]`);
                })
            }
            else  
                throw Error("Error creating an order")
        })())
        .catch(e=>{console.error(e); notifyFailier("Failed to create your order")});
    }

    const handleChange = (e)=>{
        const {name, value} = e.target;
        setFormVariables({...formVariables, [name]: value});
    }

    const deleteFromCart = async(variantId)=>{
        await fetch (`http://localhost:8081/api/cart/delete/${variantId}`, {
            method:"DELETE",
            headers:{
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then (response=>response.status==200||response.status==201?(()=>notifySuccess("Product deleted succeffully"))(): (()=>{throw Error("Error deleting product from cart")})())
        .catch(e=>{console.error(e); notifyFailier("Failed to delete element from cart")});
        location.reload();
    }

    const clearCart = async ()=>{
        await fetch(`http://localhost:8081/api/cart/empty`,{
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
                    <div style={{width:"90%", marginRight:"1.5rem", display:"flex", alignItems:"center"}} onClick={()=>{navigate(`/userSession/product/other/${product.element.productId}/${product.element.variantId}`)}}>
                        <img src={product.element.images[0]?product.element.images[0]:logoIcon} alt="VariantImage" style={{marginRight:"1rem", width:"8rem", height:"8rem"}}/>
                        <div style={{fontSize:"1.5rem"}}>
                            <p style={{margin:"0"}}>{product.element.description}</p>
                            <p style={{margin:"0"}}>Color: {product.element.color}</p>
                            <p style={{margin:"0"}}>Size: {product.element.size}</p>
                            <p style={{margin:"0"}}>Price: {product.element.price} LE</p>
                            <p style={{margin:"0"}}>{product.element.state}</p>
                            <p style={{margin:"0"}}>Amount: {product.amount}</p>
                        </div>
                    </div>
                    <img src={binIcon} className="backButton" alt="binImage" onClick={()=>deleteFromCart(product.element.variantId)}/>
                </div>
            ))}
        </div>
        {productsInCart.length!=0 &&<center>
            <img onClick={()=>setIsOrdering(true)} style={{marginRight:"1rem"}} src={createOrderIcon} title="Create order" className="backButton"/>
            <img onClick={clearCart} title="Empty the whole cart" src={deleteCartIcon} alt="clearCartIcon" className="backButton"/>
        </center>}
        {productsInCart.length==0 && <center>
        <img src={emptyCartIcon} alt="emptyCartImage"/> 
        <h1>Looks like you have not added anything to you cart. Go
        ahead & explore top categories.</h1>
        </center>}
        <Modal
        isOpen={isOrdering}
        onRequestClose={()=>setIsOrdering(false)}
        style={{content:{width:"50%", height:"50%",top: "50%",
            left: "50%",
            right: "auto", 
            bottom: "auto", 
            marginRight: "-50%",
            transform: "translate(-50%, -50%)",
            borderRadius:"20px"
        }}}
        >
        {isLoading?
        <center>
        <AnimatePresence>
          <ObjectToAppear size={100}/>
        </AnimatePresence></center>:<>
            <center><h1>How would you like to pay ...?</h1></center>
        <form onSubmit={buy}>
        <label htmlFor="paymentMethod" style={{marginRight:"0.5rem"}}>Payment Method:</label>
        <select name="paymentMethod" onChange={handleChange} required>
            <option value="" disabled selected>Select payment method</option>
            <option value="VISA">VISA Card</option>
            <option value="CASH">Cash</option>
        </select><br/>
        <label htmlFor="address" style={{marginRight:"0.5rem"}}>Address:</label>
        <input type="text" name="address" onChange={handleChange} value={formVariables.address} placeholder="Address" required/>
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
        name="cvv"
        maxLength="3"
        value={formVariables.CVV}
        onChange={handleChange}
        placeholder="cvv"
        pattern="\d{3}$"
        title="Write the CVV three numbers on your credit card"
        required
      /></>
        }
        <center>
            <button type="submit" className="backButton">Buy</button>
            <button onClick={()=>setIsOrdering(false)} style={{marginLeft:"1rem"}} className="backButton">Cancel</button>
        </center>
        </form></>}
        </Modal>
    </>
    );
};
export default CartPage;