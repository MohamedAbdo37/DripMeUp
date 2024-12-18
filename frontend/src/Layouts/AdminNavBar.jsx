import settings from '../assets/settings.png';
import add from '../assets/add.png';
import addAdmin from '../assets/addAdmin.png';
import home from '../assets/home.png';
import logout from '../assets/logout.png';
import '../style.css';
import { Outlet, useNavigate } from 'react-router-dom';
const AdminNavBar = ()=>{
    const navigate = useNavigate();
    const handleLogout = ()=>{
        localStorage.removeItem('drip_me_up_jwt');
        navigate('/');
    }

    return(
        <>
            <div className="navBar">
                <input type="text" placeholder="Search" />
                <div className='navBarButtons'>
                <img src={home} alt='homeIcon' onClick={ ()=>navigate('/adminSession') }/>
                </div>
                <div className='navBarButtons'>
                <img src={add} alt='addProductIcon' onClick={ ()=>{}}/>
                </div>
                <div className='navBarButtons'>
                <img src={settings} alt='settingsIcon' onClick={ ()=>navigate('/adminSession/settings') }/>
                </div>
                <div className='navBarButtons'>
                <img src={addAdmin} alt='profileIcon' style={{width:"4rem", height: "4rem"}} onClick={ ()=>navigate('/adminSession/addAdmin') }/>
                </div>
                <div className='navBarButtons'>
                <img src={logout} alt='logoutIcon' onClick={ handleLogout }/>
                </div>
            </div>
            <Outlet/>
        </>
    );
};
export default AdminNavBar;