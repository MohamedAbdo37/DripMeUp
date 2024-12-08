import { useState, useEffect } from "react";
import { Link, useNavigate} from 'react-router-dom';
import '../style.css';

const AdminLoginBox = () =>{
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');

    const navigate = useNavigate();

    const login = async (e)=>{
        e.preventDefault()
        const adminFetched = await fetch(`http://localhost:8081/admins/login/${username}_${password}`)
        .then(response=>{response.status==200 || Response.status==201?(() => { return response.json() })():(() => { throw new Error('Something went wrong'); })()})
        .then((adminData)=>{
            setErrorMessage('');
            navigate('/admin/profile', {state: {admin: adminData}})
         })
        .catch(error=>{
            setErrorMessage('Wrong username or password');
            setErrorTrigger('usernameError');
        });
    }
    return(
        <div className="formBox">
            <form id="loginForm" onSubmit={login}>
                <header>Admin Login</header>
                <label htmlFor='username'><b>Username</b></label>
                <input type="text" name='username' placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} required></input>
                <label htmlFor="password"><b>Password</b></label>
                <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} required></input>
                {errorTrigger == "usernameError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <button className="loginButton" form="loginForm" type="submit">Login</button>
            </form>
        </div>
    );
};
export default AdminLoginBox;
