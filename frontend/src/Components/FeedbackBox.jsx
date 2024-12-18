import unknownPhoto from '../assets/unknown.jpg'; // Adjust the path as necessary
import { useEffect } from 'react';
const FeedbackBox = ({ feedback }) =>{
    useEffect(()=>{
        console.log(feedback);
    }, []);
    return(
        <div className='feedbackBox'>
            <img className="reviewPhoto" src={feedback.user.photo || unknownPhoto} alt='UserPhoto'/>
            <div className='feedbackData'>
                <p style={{margin: "0"}}>{feedback.user.name}</p>
                <p style={{margin: "0"}}>{feedback.feedback}</p>
                <p style={{margin: "0", fontSize: "1rem"}}>{feedback.time}</p>
            </div>
        </div>
    );
};
export default FeedbackBox;