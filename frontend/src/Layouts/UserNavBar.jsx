import settings from '../assets/settings.png';
import profile from '../assets/profile.png';
import favourite from '../assets/favourite.png';
import home from '../assets/home.png';
import cart from '../assets/shopping.png';
import '../style.css';
import { Outlet, useNavigate } from 'react-router-dom';
const UserNavBar = ()=>{
    const navigate = useNavigate();
    return(
        <>
            <div className="navBar">
                <input type="text" placeholder="Search" />
                <div className='navBarButtons'>
                <img src={home} alt='homeIcon' onClick={ ()=>navigate('/userSession') }/>
                </div>
                <div className='navBarButtons'>
                <img src={favourite} alt='favouriteIcon' onClick={ ()=>navigate('/userSession/favourites') }/>
                </div>
                <div className='navBarButtons'>
                <img src={cart} alt='cartIcon' onClick={ ()=>navigate('/userSession/cart') }/>
                </div>
                <div className='navBarButtons'>
                <img src={settings} alt='settingsIcon' onClick={ ()=>navigate('/userSession/settings') }/>
                </div>
                <div className='navBarButtons'>
                <img src={profile} alt='profileIcon' onClick={ ()=>navigate('/userSession/profile') }/>
                </div>
            </div>
            <Outlet/>
        </>
    );
};
export default UserNavBar;