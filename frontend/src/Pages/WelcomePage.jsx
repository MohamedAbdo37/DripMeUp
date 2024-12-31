import LoginBox from "../Components/LoginBox"
import "../style.css"
import googleLogo from '../assets/logo.png'
import Cookies from 'js-cookie';
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "../Components/ObjectToAppear";
import { Link, useNavigate } from 'react-router-dom';
import { useState } from "react";

const WelcomePage = () =>{
    const navigate = useNavigate();
    const [shouldShowObject, setShouldShowObject] = useState(false);
    const generateID = ()=>{
        let generatedID = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 10; i++) {
            generatedID += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        return (generatedID);
    }

    const handleShopping = async () =>{
        setShouldShowObject(()=>true);
        let guestID = Cookies.get('dripMeUpGuestID');
        let guest = {};
        if (!guestID){
            guestID = generateID();
            console.log("guestID:", guestID);
            Cookies.set('dripMeUpGuestID', (guestID).toString());
            const pushGuestID = await fetch(`http://localhost:8081/guests/`,{
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    guestID: guestID,
                    data: 'no data yet',
                    picture: '../assets/guest.png'
                })
            }).catch(error=>{console.log(error);setShouldShowObject(()=>false);});
            guest = {guestID: guestID,data: 'no data yet'};
        }else{
            console.log(guestID);
            const getGuest = await fetch(`http://localhost:8081/guests?guestID=${guestID}`)
            .then(Response=>Response.json())
            .then(async(data)=>{
                if (data.length == 0){
                    const pushGuestID = await fetch(`http://localhost:8081/guests/`,{
                        method: 'POST',
                        headers:{
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            guestID: guestID,
                            data: 'no data yet',
                            picture: 'https://th.bing.com/th/id/OIP.qcjhP7DA8HG_kIRvZDoDvQHaHa?rs=1&pid=ImgDetMain'
                        })
                    }).catch(error=>console.log(error));
                    guest = {guestID: guestID,data: 'no data yet'};
                }
                else{
                    guest = data[0];
                }
            })
            .catch(error=>console.log(error));
        }
        navigate('/userSession');
    }
    return(
        shouldShowObject ?
        <AnimatePresence>
            <ObjectToAppear size={100}/>
        </AnimatePresence>:
        <div className="welcomePage">
            <div className="container">
                <center><h1 style={{color:"white", animation:"slide-in 1s easy-in"}}>Welcome</h1></center>
                <center><img src={googleLogo} alt="" className="logoImage"/></center>
                <div className="buttons">
                    <button className="shopButton" onClick={handleShopping}>Shop Now</button>
                    <Link className="signupButton" to="/signup">Signup</Link>
                    <Link className="signupButton" to="/admin/login">Login as Admin</Link>
                </div>
            </div>
            <div style={{backgroundColor: "#ffffff", height:"100%",
                backgroundImage: "repeating-radial-gradient(circle at 0 0, transparent 0, rgba(255, 255, 255, 0.5) 14px), repeating-linear-gradient(rgba(10, 10, 10, 0.5), rgba(10, 10, 10, 0.5))"}}>
            <LoginBox/>
            </div>
        </div>
    );
};
export default WelcomePage;