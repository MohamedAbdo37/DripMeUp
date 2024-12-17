import { useState, useEffect } from "react";
import { Link, useNavigate} from 'react-router-dom';
import '../style.css';
import {jwtDecode} from 'jwt-decode';
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "./ObjectToAppear";
const LoginBox = () =>{
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');
    const [shouldShowObject, setShouldShowObject] = useState(false);
    const navigate = useNavigate();

    const handleCallbackResponse = async (response)=>{
        var user = jwtDecode(response.credential);
        setShouldShowObject(()=>true);
        const loginUser = await fetch(`http://localhost:8081/api/5/users/g/login`, {
                method: "GET",
                headers:{
                    'IDToken': response.credential
                }
            }
        )
        .then(response=>response.status==200 || response.status==201?(() => { return response.text() })():(() => { throw new Error('Something went wrong'); })())
        .then((data)=>{
            localStorage.setItem('drip_me_up_jwt', data);
            setErrorMessage('');
            navigate('/userSession');
        })
        .catch(async (error)=>{
            setShouldShowObject(()=>false);
            console.log(error);
            setErrorMessage('Email does not exist in the system');
            setErrorTrigger('googleEmailError');
        });
      } 
      useEffect(()=>{
    
        google.accounts.id.initialize({
          client_id: "717049175258-p8ir5a7n56utrq80bc4o2roi8oaf1ulh.apps.googleusercontent.com", 
          callback: handleCallbackResponse
        });
    
        google.accounts.id.renderButton(
          document.getElementById("loginGoogleButton"),
          {theme: "outline", size: "larg"}
        );
    
        google.accounts.id.prompt();
      }, [shouldShowObject]);

    const login = async (e)=>{
        e.preventDefault();
        setShouldShowObject(()=>true);
        const dummy_user = {
            username: "name",
        }
        
        const userFetched = await fetch(`http://localhost:8081/api/5/users/login`,{
            method: "GET",
            headers: {
                Email: email,
                Password: password
            }
        })
        .then(response=>response.status==200 || response.status==201?(() => { return response.text()})():(() => { throw new Error('Something went wrong'); })())
        .then((data)=>{
            localStorage.setItem('drip_me_up_jwt', data);
            console.log(data)
            setErrorMessage('');
            navigate('/userSession')
        })  
        .catch(async(error)=>{
            setShouldShowObject(()=>false);
            console.log(error);
            setErrorMessage('Wrong email or password');
            setErrorTrigger('emailError');
        }); 
    }
    return(
        shouldShowObject ?
        <AnimatePresence>
            <ObjectToAppear />
        </AnimatePresence>
        :<div className="formBox">
            <form id="loginForm" onSubmit={login}>
                <header>Login</header>
                <label htmlFor='email'><b>Email</b></label>
                <input type="email" name='email' placeholder="Email" value={email} onChange={(e)=>setEmail(e.target.value)} required></input>
                <label htmlFor="password"><b>Password</b></label>
                <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} required></input>
                {errorTrigger == "emailError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <Link className="forgotPasswordLink" to="/forgotpassword">Forgot Password?</Link>
                <button className="loginButton" form="loginForm" type="submit">Login</button>
            </form>
            <p className="lineWithText"><span>Or</span></p>
            <div id="loginGoogleButton"></div>
            {errorTrigger == "googleEmailError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
            <center>New user? <Link className="signupLink" to="/signup">Signup</Link></center>
        </div>
        
    );
};
export default LoginBox;
