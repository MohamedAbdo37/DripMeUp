import {Link} from 'react-router-dom';
import '../style.css'
const UnauthorizedPage = () =>{

    return(
        <>
            <center>
                <h1> 401! You are Unauthorized</h1>
                <Link to={'/'} className='backButton'>Go Home</Link>
            </center>
            
        </>
    );
};
export default UnauthorizedPage;