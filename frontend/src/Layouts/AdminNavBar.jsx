import settings from '../assets/settings.png';
import add from '../assets/add.png';
import addAdmin from '../assets/addAdmin.png';
import home from '../assets/home.png';
import category from '../assets/category.png';
import '../style.css';
import { Outlet, useNavigate } from 'react-router-dom';
const AdminNavBar = ()=>{
    const navigate = useNavigate();
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
                <img src={category} alt='addCategoryIcon' style={{scale:"1.9"}} onClick={ ()=>{}}/>
                </div>
                <div className='navBarButtons'>
                <img src={settings} alt='settingsIcon' onClick={ ()=>navigate('/adminSession/settings') }/>
                </div>
                <div className='navBarButtons'>
                <img src={addAdmin} alt='profileIcon' style={{length: "10rem", height: "10rem"}} onClick={ ()=>navigate('/adminSession/profile') }/>
                </div>
            </div>
            <Outlet/>
        </>
    );
};
export default AdminNavBar;