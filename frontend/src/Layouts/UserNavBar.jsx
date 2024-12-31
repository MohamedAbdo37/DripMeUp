import settings from '../assets/settings.png';
import profile from '../assets/profile.png';
import favourite from '../assets/favourite.png';
import home from '../assets/home.png';
import cart from '../assets/shopping.png';
import orders from '../assets/orders.png';
import '../style.css';
import { Outlet, useNavigate } from 'react-router-dom';
const UserNavBar = ()=>{
    const navigate = useNavigate();
    return(
        <>
            <div className="navBar">
                {/* <input type="text" placeholder="Search" /> */}
                <div className='navBarButtons'>
                <img src={home} alt='homeIcon' title="Go to home" onClick={ ()=>navigate('/userSession') }/>
                </div>
                <div className='navBarButtons'>
                <img src={favourite} alt='favouriteIcon' title="Go to favourits" onClick={ ()=>navigate('/userSession/favourites') }/>
                </div>
                <div className='navBarButtons'>
                <img src={cart} alt='cartIcon' title="Go to cart" onClick={ ()=>navigate('/userSession/cart') }/>
                </div>
                <div className='navBarButtons'>
                <img src={orders} alt='ordersIcon' title="Go to your orders" style={{width:"4rem", height: "4rem"}} onClick={ ()=>navigate('/userSession/my-orders') }/>
                </div>
                <div className='navBarButtons'>
                <img src={profile} alt='profileIcon' title="Go to profile" onClick={ ()=>navigate('/userSession/profile') }/>
                </div>
            </div>
            <Outlet/>
        </>
    );
};
export default UserNavBar;