import { useState, useEffect } from "react";
import { Link, useNavigate} from 'react-router-dom';
import { toast } from "react-toastify";

import '../style.css';

const AddAdminBox = () =>{
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTrigger, setErrorTrigger] = useState('');
    const navigate = useNavigate();
    
    const notifyAddedAdmin = () => {
      toast.success(`New admin added successfully`);
    };
    const notifyFailAddedAdmin = () => {
      toast.error(`Failed to add a new admin`);
    };
    const add = async (e)=>{
        e.preventDefault()
        await fetch(`http://localhost:8081/api/6/admin/signup`, {
            method: "POST",
            headers:{
                'UserName': username,
                'Password': password,
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
        })
        .then(response=>response.status==200 || response.status==201?(() => { notifyAddedAdmin() })():(() => { throw new Error('Failed to add admin'); })())
        .catch(error=>{
            notifyFailAddedAdmin();
            console.log(error);
        });
    }
    return(
        <div className="formBox" style={{marginTop: "10rem"}}>
            <form id="loginForm" onSubmit={add}>
                <header>Create Admin </header>
                <label htmlFor='username'><b>Username</b></label>
                <input type="text" name='username' placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} required></input>
                <label htmlFor="password"><b>Password</b></label>
                <input type="password" name='password' placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} required></input>
                {errorTrigger == "usernameError"?<p style={{color:'red', fontSize:'1rem'}}>{errorMessage}</p>:<></>}
                <button className="loginButton" form="loginForm" type="submit">Add</button>
            </form>
        </div>
    );
};
export default AddAdminBox;
