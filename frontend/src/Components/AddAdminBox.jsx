import { useState, useEffect } from "react";
import { Link, useNavigate} from 'react-router-dom';
import '../style.css';
import {jwtDecode} from 'jwt-decode';

const AddAdminBox = () =>{
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');
    const navigate = useNavigate();
    const { superID } = location.state || {};

    const addAdmin = async (e)=>{
        e.preventDefault()
        const loginUser = await fetch(`http://localhost:8081/api/6/admin/signup`, {
            method: "POST",
            headers:{
                'UserName': username,
                'Password': password,
                'SuperID': superID
            }
        })
        .then(response=>{response.status==200 || response.status==201?(() => { return response.json() })():(() => { throw new Error('Something went wrong'); })()})
        .then((data)=>{
            setErrorMessage('');
        })
        .catch(async (error)=>{
            setErrorMessage('Username already exists in the system');
            setErrorTrigger('usernameError');
        });
    }
    return(
        <div className="formBox">
            <form id="loginForm" onSubmit={addAdmin}>
                <header>Login</header>
                <label htmlFor='username'><b>Username</b></label>
                <input type="text" name='username' placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} required></input>
                <label htmlFor="password"><b>Password</b></label>
                <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} required></input>
                {errorTrigger == "usernameError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <button className="loginButton" form="loginForm" type="submit">Add Admin</button>
            </form>
        </div>
    );
};
export default AddAdminBox;
