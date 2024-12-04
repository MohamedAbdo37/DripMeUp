import LoginBox from "../Components/LoginBox"
import "../style.css"
import googleLogo from '../assets/logo.png'
import Cookies from 'js-cookie';

import { Link, useNavigate } from 'react-router-dom';

const WelcomePage = () =>{
    const navigate = useNavigate();

    const generateID = ()=>{
        let generatedID = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 10; i++) {
            generatedID += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        return (generatedID);
    }

    const handleShopping = async () =>{
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
            }).catch(error=>console.log(error));
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
        navigate('/homepage', {state: {user: guest, userType: "guest"}});
    }
    return(
        <div className="welcomePage">
            <div className="container">
                <center><h1>Welcome</h1></center>
                <center><img src={googleLogo} alt="" className="logoImage"/></center>
                <div className="buttons">
                    <button className="shopButton" onClick={handleShopping}>Shop Now</button>
                    <Link className="signupButton" to="/signup">Signup</Link>
                    <Link className="signupButton" to="/admin/login">Login as Admin</Link>
                </div>
            </div>
            <LoginBox/>
        </div>
    );
};
export default WelcomePage;