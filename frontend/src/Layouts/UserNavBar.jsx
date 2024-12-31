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
                <img src={home} alt='homeIcon' title="Go to home" style={{width:"3rem", height: "3rem"}} onClick={ ()=>navigate('/userSession') }/>
                </div>
                <div className='navBarButtons'>
                <img src={favourite} alt='favouriteIcon' title="Go to favourits" style={{width:"3rem", height: "3rem"}} onClick={ ()=>navigate('/userSession/favourites') }/>
                </div>
                <div className='navBarButtons'>
                <img src={cart} alt='cartIcon' title="Go to cart" style={{width:"3rem", height: "3rem"}} onClick={ ()=>navigate('/userSession/cart') }/>
                </div>
                <div className='navBarButtons'>
                <img src={orders} alt='ordersIcon' title="Go to your orders" style={{width:"3rem", height: "3rem"}} onClick={ ()=>navigate('/userSession/my-orders') }/>
                </div>
                <div className='navBarButtons'>
                <img src={profile} alt='profileIcon' title="Go to profile" style={{width:"3rem", height: "3rem"}} onClick={ ()=>navigate('/userSession/profile') }/>
                </div>
            </div>
            <Outlet/>
        </>
    );
};
export default UserNavBar;