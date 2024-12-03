import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import '../style.css';
import 'react-phone-number-input/style.css'
import PhoneInput, {isValidPhoneNumber} from 'react-phone-number-input'
import countryNames from 'react-phone-number-input/locale/en'
import {jwtDecode} from 'jwt-decode';
import emailjs from 'emailjs-com';


const SignupBox = () =>{
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confermPassword, setConfermPassword] = useState("");
    const [country, setCountry] = useState("+20");
    const [phone, setPhone] = useState("");
    const [city, setCity] = useState("");
    const [address, setAddress] = useState("");
    const [gender, setGender] = useState("");
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');
    const [phase, setPhase] = useState(1);
    const [code, setCode] = useState('');
    const [trueCode, setTrueCode] = useState('');
    const navigate = useNavigate();

    const handleCallbackResponse = async (response)=>{
        var user = jwtDecode(response.credential);
        // const checkAdminEmail = await fetch(`http://localhost:8081/admins/check/${user.email}`)
        // .then(response=>response.json())
        // .then(async (data)=>{
        //     if (data == 'not found'){
                console.log(user);
                const register = await fetch(`http://localhost:8081/users/signup`,{
                    method: 'POST',
                    headers:{
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: user.name,
                        email: user.email,
                        password: '',
                        phone: '',
                        country: '',
                        city: '',
                        address: '',
                        gender: '',
                        picture: ''
                    })
                })
                .then(Response=>Response=>Response.status==200 || Response.status==201? navigate('/homepage', 
                    {state: {user: {
                                        username: user.name,
                                        email: user.email,
                                        password: '',
                                        phone: '',
                                        country: '',
                                        city: '',
                                        address: '',
                                        gender: '',
                                        picture: ''
                                    },
                            userType: "user"}
                    }):(() => { throw new Error('Something went wrong'); })())
                .catch(error=>{
                    setErrorMessage('Email already exists in the system');
                    setErrorTrigger('googleEmailError');
                });
            // }
            // else{
            //     setErrorMessage('Email already exists in the system');
            //     setErrorTrigger('googleEmailError');
            // }
        // })
        // .catch(error=>console.log(error));
      } 
      useEffect(()=>{
    
        google.accounts.id.initialize({
          client_id: "717049175258-p8ir5a7n56utrq80bc4o2roi8oaf1ulh.apps.googleusercontent.com", 
          callback: handleCallbackResponse
        });
    
        google.accounts.id.renderButton(
          document.getElementById("signupGoogleButton"),
          {theme: "outline", size: "larg"}
        );
    
        google.accounts.id.prompt();
      }, []);
      
    const generateCode = ()=>{
        let generatedCode = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 4; i++) {
            generatedCode += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        return (generatedCode);
    }

    const sendCode = async (e)=>{
        e.preventDefault();
        let c = generateCode();
        console.log(c)
        setTrueCode(c);
        if (phone && isValidPhoneNumber(phone)){                    
            setPhase(2);
            emailjs
                .send(
                    'service_j4cifp3', // Replace with your EmailJS Service ID
                    'template_zlx3hfj', // Replace with your EmailJS Template ID
                    {email: email, to_name: u, code: c},
                    '6nj8Z27gLH-R_ZFsc' // Replace with your EmailJS User ID
                )
                .then(
                    (result) => {
                        console.log('Email sent successfully!');
                        setPhase(2);
                    },
                    (error) => {
                        alert('Failed to send email.');
                    }
                );
        }else{
            setErrorMessage("Phone number is not correct");
            setErrorTrigger("phoneError");
        }
    }

    const checkCode = (e)=>{
        e.preventDefault()
        if (trueCode == code){
            setPhase(1);
            setErrorMessage("");
            signup()
        }
        else
            setErrorMessage("Wrong Code, Try again or click resend");
    }

    const signup = async (e)=>{
        e.preventDefault();
        // const checkEmail = await fetch(`http://localhost:8081/admins/check/${email}`)
        // .then((Response) => Response.json())
        // .then(async (data)=>{
        //     if (data == 'found'){
        //         setErrorMessage("Email already exists");
        //         setErrorTrigger("emailError");
        //     }
        //     else{
                const register = await fetch(`http://localhost:8081/users/signup`,{
                    method: 'POST',
                    headers:{
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        userName: username,
                        email: email,
                        password: password,
                        phone: phone,
                        country: countryNames[country],
                        city: city,
                        address: address,
                        gender: gender,
                        picture: ''
                    })
                 })
                .then(Response=>Response.status==200 || Response.status==201?navigate('/homepage', 
                    {state: {user: {
                                        userName: username,
                                        email: email,
                                        password: password,
                                        phone: phone,
                                        country: countryNames[country],
                                        city: city,
                                        address: address,
                                        gender: gender,
                                        picture: ''
                                    },
                            userType: "user"}
                    }):(() => { throw new Error('Something went wrong'); })())
                .catch(error=>{
                    setErrorMessage("Email already exists");
                    setErrorTrigger("emailError");
                });
        //     }
        // })
        // .catch(error=>console.log(error));
    }
    
    return(
        <div className="formBox">
            {phase==1 &&(
                <form id="signupForm" onSubmit={sendCode}>
                    <header>Signup</header>
                    <label htmlFor='username'><b>Username</b></label>
                    <input type="text" name='username' placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} required></input>
                    <label htmlFor="email"><b>Email</b></label>
                    <input type="text" name='email' placeholder="Email" value={email} onChange={(e)=>setEmail(e.target.value)} required></input>
                    {errorTrigger == "emailError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                    <label htmlFor="password"><b>Password</b></label>
                    <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$" required></input>
                    {/(?=.*[a-z])/.test(password) ?
                        <p style={{color: 'green', fontSize:'1rem'}}>• at least one lowercase letter is present.</p> :
                        <p style={{color: 'black', fontSize:'1rem'}}>• at least one lowercase letter is present.</p>
                    }
                    {/(?=.*[A-Z])/.test(password) ?
                        <p style={{color: 'green', fontSize:'1rem'}}>• at least one uppercase letter is present.</p> :
                        <p style={{color: 'black', fontSize:'1rem'}}>• at least one uppercase letter is present</p>
                    }
                    {/(?=.*\d)/.test(password) ?
                        <p style={{color: 'green', fontSize:'1rem'}}>• at least one digit is present.</p> :
                        <p style={{color: 'black', fontSize:'1rem'}}>• at least one digit is present.</p>
                    }
                    {/(?=.*[@$!%*?&])/.test(password) ?
                        <p style={{color: 'green', fontSize:'1rem'}}>• at least one special character from the set @$!%*?& is present.</p> :
                        <p style={{color: 'black', fontSize:'1rem'}}>• at least one special character from the set @$!%*?& is present.</p>
                    }
                    {/[A-Za-z\d@$!%*?&]{8,}/.test(password) ?
                        <p style={{color: 'green', fontSize:'1rem'}}>• at least 8 characters.</p> :
                        <p style={{color: 'black', fontSize:'1rem'}}>• at least 8 characters.</p>
                    }
                    <label htmlFor="confermPassword"><b>Conferm Password</b></label>
                    <input type="password" name='confermPassword' placeholder="Conferm Password" value={confermPassword} onChange={(e)=>setConfermPassword(e.target.value)} pattern={password} required></input>
                    {confermPassword == password ?
                        <></> :
                        <p style={{color: 'red', fontSize:'1rem'}}>Conferm password doesn't match the enterd password</p>
                    }
                    <label htmlFor="phone"><b>Phone</b></label>
                    <PhoneInput className="phoneInput" international placeholder="Enter phone number" onCountryChange={setCountry} value={phone} onChange={setPhone} isValidPhoneNumber required/>
                    {errorTrigger == "phoneError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                    <label htmlFor="city"><b>City</b></label>
                    <input type="text" name='city' placeholder="City" value={city} onChange={(e)=>setCity(e.target.value)} required></input>
                    <label htmlFor="address"><b>Address</b></label>
                    <input type="text" name='address' placeholder="Address" value={address} onChange={(e)=>setAddress(e.target.value)} required></input>
                    <label htmlFor="gender"><b>Gender</b></label>
                    <div className="gender">
                        <div>
                            <label htmlFor="male">Male</label>
                            <input type="radio" name='gender' onClick={()=>setGender("Male")} required></input>
                        </div>
                        <div>
                            <label htmlFor="female">Female</label>
                            <input type="radio" name='gender' onClick={()=>setGender("Female")}></input>
                        </div>
                    </div>
                    <button className="signupButton" type="submit" form="signupForm">Signup</button>
                </form>
            )}
            {phase==1 && (
                <>
                <p className="lineWithText"><span>Or</span></p>
                <div id="signupGoogleButton"></div>
                {errorTrigger == "googleEmailError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <center>Already have an account? <Link className="loginLink" to="/login">Login</Link></center>
                </>
            )}
            {phase==2 && (
                <form id="codeForm" onSubmit={checkCode}>
                    <p style={{fontSize:'1rem'}}>{username} we have sent you a code of 4 characters on your email, please check your inbox (or your spam).</p>
                    <label htmlFor="code"><b>Code</b></label>
                    <input type="text" name='code' placeholder="Enter Code" value={code} onChange={(e)=>setCode(e.target.value)} required></input>
                    <div style={{display:'flex', justifyContent: 'space-between'}}>
                        <p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>
                        <button className="resendCodeLink" onClick={sendCode}>Resend code</button>
                    </div>
                    <button className="loginButton" form="codeForm" type="submit">Conferm Code</button>
                </form>
            )
        }
        </div>
    );
};
export default SignupBox;
