import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import '../style.css';
import 'react-phone-number-input/style.css'
import PhoneInput, {isValidPhoneNumber} from 'react-phone-number-input'
import {jwtDecode} from 'jwt-decode';
import { toast } from "react-toastify";
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "../Components/ObjectToAppear";

const SignupBox = () =>{
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confermPassword, setConfermPassword] = useState("");
    const [phone, setPhone] = useState("");
    const [gender, setGender] = useState("UNKNOWN");
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');
    const [phase, setPhase] = useState(1);
    const [code, setCode] = useState('');
    const [trueCodeID, setTrueCodeID] = useState('');
    const [isDisabled, setIsDisabled] = useState(true);
    const [timer, setTimer] = useState(0);
    const [shouldShowObject, setShouldShowObject] = useState(false);

    const navigate = useNavigate();

    useEffect(()=>{
    
        if (timer < 59 && !isDisabled){
            const interval = setInterval(() => {
                setTimer((prev) =>prev+1);
            }, 1000);
            return ()=>clearInterval(interval);
        }
        else if (!isDisabled)
            sendCode(new Event("Resend code")); 

        google.accounts.id.initialize({
          client_id: "717049175258-p8ir5a7n56utrq80bc4o2roi8oaf1ulh.apps.googleusercontent.com", 
          callback: handleCallbackResponse
        });
    
        google.accounts.id.renderButton(
          document.getElementById("signupGoogleButton"),
          {theme: "outline", size: "larg"}
        );
    
        google.accounts.id.prompt();
      }, [[timer, isDisabled, shouldShowObject]]);

    const resetTimer=()=>{
        setTimer(0);
    }

    const notifySentCode = () => {
        toast.success(`Code was sent to ${email} successfully`);
    };

    const notifyFailSentCode = () => {
        toast.error(`Failed to send code to ${email}`);
    };

    const handleCallbackResponse = async (response)=>{
        setShouldShowObject(()=>true);
        var user = jwtDecode(response.credential);
        console.log(user);
        const register = await fetch(`http://localhost:8081/api/5/users/g/signup`,{
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                'IDToken': response.credential
            },
            body: JSON.stringify({
                userID: '',
                userName: user.name,
                email: user.email,
                password: '',
                phone: '',
                gender: 'UNKNOWN',
                photo: '',
                description: '',
                theme: 'LIGHT'
            })
         })
        .then(Response=>Response.status==200 || Response.status==201? (() => { return Response.text() })():(() => { throw new Error('Something went wrong');})())
        .then((data)=>{
            localStorage.setItem('drip_me_up_jwt', data);
            console.log(data)
            setErrorMessage('');
            navigate('/userSession')
        })
        .catch(error=>{
            setShouldShowObject(()=>false);
            console.log(error);
            setErrorMessage('Email already exists in the system');
            setErrorTrigger('googleEmailError');
        });
      } 
      
    const generateCode = ()=>{
        let generatedCode = '';
        const characters = '0123456789';
        for (let i = 0; i < 4; i++) {
            generatedCode += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        return (generatedCode);
    }

    const sendCode = async (e)=>{
        setShouldShowObject(()=>true);
        e.preventDefault();
        setPhase(2);
        setIsDisabled(true);
        if (phone && isValidPhoneNumber(phone)){  
            const getCodeID = await fetch(`http://localhost:8081/api/5/users/signup/code`, {
                method: 'GET',
                headers:{
                    'Email': email,
                    'UserName': username
                }
            })
            .then(response=>response.status == 200 || response.status == 201? (()=>{return response.json()})() : (()=>{throw Error("Error sending code")})())
            .then(codeID=>{
                setShouldShowObject(()=>false);
                setTrueCodeID(codeID);
                console.log('Email sent successfully!');
                setIsDisabled(false);
                notifySentCode();
                resetTimer();
            })
            .catch(e=>{
                setShouldShowObject(()=>false);
                notifyFailSentCode();
                console.log(e);
            });
           
        }else{
            setShouldShowObject(()=>false);
            setErrorMessage("Phone number is not correct");
            setErrorTrigger("phoneError");
        }
    }

    const checkCode = async(e)=>{
        e.preventDefault()
        setShouldShowObject(()=>true);
        const checkCode = await fetch(`http://localhost:8081/api/5/users/checkCode`,{
            method:'GET',
            headers:{
                'CodeID': trueCodeID,
                'Code': code
            }
        })
        .then(response=>response.status==200 || response.status==201 ? (()=>{
            setPhase(1);
            setErrorMessage("");
            signup();
        })() : (()=>{setErrorMessage("Wrong Code, Try again or click resend");})())
        .catch(e=>{console.log(e);setShouldShowObject(()=>false);});
    }


    const signup = async ()=>{
        let userID = generateCode();
        const register = await fetch(`http://localhost:8081/api/5/users/signup`,{
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                'UserID': userID
            },
            body: JSON.stringify({
                userID: userID,
                userName: username,
                email: email,
                password: password,
                phone: phone,
                gender: gender,
                photo: '',
                description: '',
                theme: 'LIGHT'
            })
         })
        .then(response=>response.status==200 || response.status==201?(() => { return response.text() })():(() => { throw new Error('Something went wrong'); })())
        .then((data)=>{
            localStorage.setItem('drip_me_up_jwt', data);
            console.log(data)
            setErrorMessage('');
            navigate('/userSession')
        })
        .catch(error=>{
            setErrorMessage("Email already exists");
            setErrorTrigger("emailError");
        });
    }
    
    return(
        shouldShowObject ?
        <AnimatePresence>
            <ObjectToAppear size={100}/>
        </AnimatePresence>:
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
                    <label htmlFor="confermPassword"><b>Confirm Password</b></label>
                    <input type="password" name='confermPassword' placeholder="Conferm Password" value={confermPassword} onChange={(e)=>setConfermPassword(e.target.value)} pattern={password} required></input>
                    {confermPassword == password ?
                        <></> :
                        <p style={{color: 'red', fontSize:'1rem'}}>Conferm password doesn't match the enterd password</p>
                    }
                    <label htmlFor="phone"><b>Phone</b></label>
                    <PhoneInput className="phoneInput" international placeholder="Enter phone number" value={phone} onChange={setPhone} isValidPhoneNumber required/>
                    {errorTrigger == "phoneError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                    
                    <label htmlFor="gender"><b>Gender</b></label>
                    <div className="gender">
                        <div>
                            <label htmlFor="male">Male</label>
                            <input type="radio" name='gender' onClick={()=>setGender("MALE")} required></input>
                        </div>
                        <div>
                            <label htmlFor="female">Female</label>
                            <input type="radio" name='gender' onClick={()=>setGender("FEMALE")}></input>
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
                    <p style={{fontSize:'1rem'}}>{username} we have sent you a code of 6 characters on your email, please check your inbox (or your spam).</p>
                    <label htmlFor="code"><b>Code</b></label>
                    <input type="text" name='code' placeholder="Enter Code" value={code} onChange={(e)=>setCode(e.target.value)} required></input>
                    <div style={{display:'flex', justifyContent: 'space-between'}}>
                        <p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>
                        <button className="resendCodeLink" onClick={sendCode}>Resend code</button>
                    </div>
                    <p style={{fontSize:"1rem"}}>Email will expire and automatically resent again after 1 min: {timer} sec</p>
                    <button className="loginButton" form="codeForm" type="submit" disabled={isDisabled}>Conferm Code</button>
                </form>
            )
        }
        </div>
    );
};
export default SignupBox;
