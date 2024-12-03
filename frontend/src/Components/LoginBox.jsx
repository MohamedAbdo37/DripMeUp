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
        const checkUserEmail = await fetch(`http://localhost:8081/users?email=${user.email}`)
        .then(response=>response.json())
        .then(async (data)=>{
            console.log(data, user.email);
            if (data.length == 0){
                const checkAdminEmail = await fetch(`http://localhost:8081/admins?email=${user.email}`)
                .then(response=>response.json())
                .then((data)=>{
                    if (data.length == 0){
                        setErrorMessage('Email does not exist in the system');
                        setErrorTrigger('googleEmailError');
                    }
                    else{
                        setErrorMessage('');
                        navigate('/homepage', {state: {user: data[0], userType: "admin"}});
                    }
                })
            }
            else{
                setErrorMessage('');
                navigate('/homepage', {state: {user: data[0], userType: "user"}});
            }
        })
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
        try{
            const userFetched = await fetch(`http://localhost:8081/users?email=${email}`);
            const user = await userFetched.json();
            if (user[0].password == password){
                setErrorMessage('');
                navigate('/homepage', {state: {user: user[0], userType: "user"}})
            }else{
                setErrorMessage('Password is not correct');
                setErrorTrigger('passwordError');
            }
        }catch(e){
            try{
                const userFetched = await fetch(`http://localhost:8081/admins?email=${email}`);
                const user = await userFetched.json();
                if (user[0].password == password){
                    setErrorMessage('');
                    navigate('/homepage', {state: {user: user[0], userType: "admin"}})
                }else{
                    setErrorMessage('Password is not correct');
                    setErrorTrigger('passwordError');
                }
            }catch(error){
                console.log("Error: fetching user: ", error);
                setErrorMessage('Email is not correct');
                setErrorTrigger('emailError');
            }
        }

    }
    const loginWithGoogle = ()=>{

    }
    return(
        <div className="formBox">
            <form id="loginForm" onSubmit={login}>
                <header>Login</header>
                <label htmlFor='email'><b>Email</b></label>
                <input type="email" name='email' placeholder="Email" value={email} onChange={(e)=>setEmail(e.target.value)} required></input>
                {errorTrigger == "emailError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <label htmlFor="password"><b>Password</b></label>
                <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} required></input>
                {errorTrigger == "passwordError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
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