import {useLocation} from 'react-router-dom';
import {useState} from 'react';
import UnauthorizedPage from './UnauthorizedPage';
const HomePage = () =>{
    const location = useLocation();
    const { userType, user } = location.state || {};
    if(userType=="user"){
        return(<>
            <center>User</center>
            <img src={user.picture} alt='' style={{borderRadius:'50%',  width: '10rem', height: '10rem'}}></img>
            <p>Email: {user.email}</p>
            <p>Username: {user.userName}</p>
            {/* <p>Phone: {user.phone}</p>
            <p>Country: {user.country}</p>
            <p>City: {user.city}</p>
            <p>Address: {user.address}</p> */}
            <p>Gender: {user.gender}</p>
        </>);
    }
    else if (userType=="guest"){
        return(<>
            <center>Guest</center>
            <img src={user.picture} alt='' style={{borderRadius:'50%', width: '10rem', height: '10rem'}}></img>
            <p>GuestID: {user.guestID}</p>
            <p>GuestData: {user.data}</p>
        </>);
    }
    else if(userType=="admin"){
        return(<>
            <center>Admin</center>
            <img src={user.picture} alt='' style={{borderRadius:'50%',  width: '10rem', height: '10rem'}}></img>
            <p>Email: {user.email}</p>
            <p>Username: {user.userName}</p>
            {/* <p>Phone: {user.phone}</p>
            <p>Country: {user.country}</p>
            <p>City: {user.city}</p>
            <p>Address: {user.address}</p> */}
            <p>Gender: {user.gender}</p>
        </>);
    }
    else{
        return(<UnauthorizedPage/>)
    }
};
export default HomePage;