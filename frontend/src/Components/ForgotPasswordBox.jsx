import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import '../style.css';

const ForgotPasswordBox = () =>{
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confermPassword, setConfermPassword] = useState('');
    const [code, setCode] = useState('');
    const [trueCodeID, setTrueCodeID] = useState('');
    const [phase,setPhase] = useState(1);
    const [errorMessage, setErrorMessage] = useState('');
    const [sessionID, setSessionID] = useState('');
    const [isDisabled, setIsDisabled] = useState(true);
    const [timer, setTimer] = useState(0);
    const[sendCodeCounter, setSendCodeCounter] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const interval = null;
        if (phase == 2){
            const interval = setInterval(() => {
                setTimer((prev) =>{
                    if (prev > 59){
                        clearInterval(interval); // Cleanup on unmount
                        sendCode(new Event("Null event"));
                        return prev;
                    }
                    else return prev + 1;
                });
            }, 1000);
        }
        return () => clearInterval(interval);
      }, [sendCodeCounter]);

    const notifyChangedPassword = () => {
        toast.success(`Password changed successfully`);
    };

    const notifySentCode = () => {
        toast.success(`Code was sent to ${email} successfully`);
    };

    const changePassword = async ()=>{
        const changePw = await fetch(`http://localhost:8081/api/5/users/changePassword`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Email': email,
                'NewPassword': password,
                'SessionID': sessionID
            }
        })
        .then(response=>response.status==200 || response.status==201?(() => { notifyChangedPassword() })():(() => { throw new Error('Something went wrong'); })())
        .catch((error)=>console.log(error));
    }
    const getUsername = async ()=>{
        let userName = null;
        const userFetched = await fetch(`http://localhost:8081/api/5/users/getUsername`,{
            method: "GET",
            headers:{
                'Email': email
            }
        })
        .then(Response=>Response.status==200 || Response.status==201? (() => { return Response.json() })():(() => { throw new Error('Something went wrong');})())
        .then((data)=>{
            setUsername(data.username);
            setErrorMessage('');
            userName = data.username;
        })
        .catch(error=>{
            setErrorMessage("This email isn't regesterd in the system");
        });
        return userName;
    }

    const sendCode = async (e)=>{
        e.preventDefault();
        setPhase(2);
        setIsDisabled(true);
        let u = await getUsername()
        console.log("userName:", u);
        if (u != null){
            const getCodeID = await fetch(`http://localhost:8081/api/5/users/forgotPassword/code`, {
                method: 'GET',
                headers:{
                    'Email': email,
                    'UserName': u
                }
            })
            .then(response=>response.status == 200 || response.status == 201? (()=>{return response.json()})() : (()=>{throw Error("Error sending code")})())
            .then(codeID=>{
                setTrueCodeID(codeID);
                console.log('Email sent successfully!');
                setIsDisabled(false);
                notifySentCode();
                setTimer(()=>0);
                setSendCodeCounter((prev)=>prev+1);
            })
            .catch(e=>alert('Failed to send email.'));
        }
    }
    const checkCode = async(e)=>{
        e.preventDefault()
        const checkCode = await fetch(`http://localhost:8081/api/5/users/checkCode`,{
            method:'GET',
            headers:{
                'CodeID': trueCodeID,
                'Code': code
            }
        })
        .then(response=>response.status==200 || response.status==201 ? (()=>{return response.json()})() : (()=>{throw Error("Error code doesn't match")})())
        .then(sessionID=>{
            setPhase(3);
            setErrorMessage("");
            setSessionID(sessionID);
        })
        .catch(e=>{
            console.log(e);
            setErrorMessage("Wrong Code, Try again or click resend");
        });
    }

    const checkPassword = async (e)=>{
        e.preventDefault()
        if (password == confermPassword){
            setErrorMessage('');
            await changePassword();
            navigate('/login');
        }else{
            setErrorMessage("Confermed password doesn't match with the enterd password");
        }
    }

    return(
        <div className="formBox">
            <header>Forgot Password</header>
            {phase==1 && (
                <form id="emailForm" onSubmit={sendCode}>
                    <label htmlFor='email'><b>Email</b></label>
                    <input type="email" name='email' placeholder="Email" value={email} onChange={(e)=>setEmail(e.target.value)} required/>
                    <p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>
                    <button className="loginButton" type="submit" form="emailForm">Send Code</button>
                </form>
            )}
            {phase==2 && (
                <form id="codeForm" onSubmit={checkCode}>
                    <p style={{fontSize:'1rem'}}>{username} we have sent you a code of 6 characters on your email, please check your inbox (or your spam).</p>
                    <label htmlFor="code"><b>Code</b></label>
                    <input type="text" name='code' placeholder="Enter Code" value={code} onChange={(e)=>setCode(e.target.value)} required></input>
                    <div style={{display:'flex', justifyContent: 'space-between'}}>
                        <p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>
                        <button className="resendCodeLink" onClick={sendCode}  disabled={isDisabled}>Resend code</button>
                    </div>
                    <p style={{fontSize:"1rem"}}>Email will expire and automatically resent again after 1 min: {timer} sec</p>
                    <button className="loginButton" form="codeForm" type="submit" disabled={isDisabled}>Confirm Code</button>
                </form>
            )}
            {phase==3 && (
                <form id="changePasswordForm" onSubmit={checkPassword}>
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
                    <p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>
                    <button className="loginButton" form="changePasswordForm" type="submit">Conferm Password</button>
                </form>
            )}
            
        </div>
    );
};
export default ForgotPasswordBox;
