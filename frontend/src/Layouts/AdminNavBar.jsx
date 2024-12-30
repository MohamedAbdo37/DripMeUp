import settings from '../assets/settings.png';
import add from '../assets/add.png';
import addAdmin from '../assets/addAdmin.png';
import home from '../assets/home.png';
import logout from '../assets/logout.png';
import '../style.css';
import { Outlet, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import { useState } from 'react';
import AddProductForm from '../Components/AddProductForm';

const AdminNavBar = ()=>{
    const navigate = useNavigate();
    const handleLogout = ()=>{
        localStorage.removeItem('drip_me_up_jwt');
        navigate('/');
    }
    const [showAddProductForm, setShowAddProductForm] = useState(false);


    return(
        <>
            <div className="navBar">
                {/* <input type="text" placeholder="Search" /> */}
                <div className='navBarButtons'>
                <img src={home} alt='homeIcon' title='Go to home' onClick={ ()=>navigate('/adminSession') }/>
                </div>
                <div className='navBarButtons'>
                <img src={add} alt='addProductIcon' title='Add product' onClick={ ()=>{setShowAddProductForm(true)}}/>
                </div>
                <div className='navBarButtons'>
                <img src={addAdmin} alt='profileIcon' title='Add admin' style={{width:"4rem", height: "4rem"}} onClick={ ()=>navigate('/adminSession/addAdmin') }/>
                </div>
                <div className='navBarButtons'>
                <img src={logout} alt='logoutIcon' title='logout' onClick={ handleLogout }/>
                </div>
            </div>
            <Outlet/>
            <Modal 
                isOpen={showAddProductForm}
                onRequestClose={()=>setShowAddProductForm(false)}
            >
                <button className="backButton" onClick={()=>setShowAddProductForm(false)}>X</button>
                <AddProductForm/>
            </Modal>
        </>
    );
};
export default AdminNavBar;