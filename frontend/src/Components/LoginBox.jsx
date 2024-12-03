import { useState, useEffect } from "react";
import { Link, useNavigate} from 'react-router-dom';
import '../style.css';
import {jwtDecode} from 'jwt-decode';

const LoginBox = () =>{
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');

    const navigate = useNavigate();

    const handleCallbackResponse = async (response)=>{
        var user = jwtDecode(response.credential);
        const loginUser = await fetch(`http://localhost:8081/users/login/${user.email}`)
        .then(response=>response.json())
        .then((data)=>{
            setErrorMessage('');
            navigate('/homepage', {state: {user: data, userType: "user"}});
        })
        .catch(async (error)=>{
            // const loginAdmin = await fetch(`http://localhost:8081/admins/login/${user.email}`)
            // .then(response=>response.json())
            // .then((data)=>{
            //     setErrorMessage('');
            //     navigate('/homepage', {state: {user: data, userType: "admin"}})
            // })
            // .catch(error=>{
            //     setErrorMessage('Email does not exist in the system');
            //     setErrorTrigger('googleEmailError');
            // });
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
      }, []);

    const login = async (e)=>{
        e.preventDefault()
        const userFetched = await fetch(`http://localhost:8081/users/login/${email}_${password}`)
        .then(response=>{console.log(response.status);return response.json();})
        .then(async(userData)=>{
            setErrorMessage('');
            navigate('/homepage', {state: {user: userData, userType: "user"}})
        })  
        .catch(async(error)=>{
            // const adminFetched = await fetch(`http://localhost:8081/admins/login/${email}_${password}`)
            // .then(response=>response.json())
            // .then((adminData)=>{
            //     setErrorMessage('');
            //     navigate('/homepage', {state: {user: adminData, userType: "admin"}})
            // })
            // .catch(error=>{
            //     setErrorMessage('Wrong email or password');
            //     setErrorTrigger('emailError');
            // });
            setErrorMessage('Wrong email or password');
            setErrorTrigger('emailError');
        }); 
    }
    return(
        <div className="formBox">
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
