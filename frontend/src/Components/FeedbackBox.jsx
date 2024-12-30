import { useState, useEffect } from 'react';
import unknownPhoto from '../assets/unknown.jpg'; // Adjust the path as necessary

const FeedbackBox = ({ feedback }) => {
    const [userProfile, setUserProfile] = useState(null);

    // Fetch user profile based on the user ID in feedback
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await fetch(`http://localhost:8081/users/${feedback.userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                });

                if (response.ok) {
                    const userData = await response.json(); // Assuming the backend returns user profile data
                    setUserProfile(userData); // Store the profile data in state
                } else if (response.status === 401) {
                    console.error('Unauthorized');
                } else if (response.status === 403) {
                    console.error('Forbidden');
                } else {
                    console.error('Failed to fetch user data');
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };


        if (feedback.userId) {
            fetchUserData(); // Fetch user profile if feedback contains user ID
        }
    }, [feedback.userId]); // Run effect when feedback.user.id changes

    return (
        <div className='feedbackBox'>
            <img
                className="reviewPhoto"
                src={userProfile?.profilePhoto || unknownPhoto} // Use the fetched user's photo or fallback to unknownPhoto
                alt='UserPhoto'
            />
            <div className='feedbackData'>
                <p style={{ margin: "0" }}>{userProfile?.username || 'Unknown User'}</p> {/* Display fetched name or fallback */}
                <p style={{ margin: "0" }}>{feedback.feedback}</p>
                <p style={{ margin: "0", fontSize: "1rem" }}>{feedback.time}</p>
            </div>
        </div>
    );
};

export default FeedbackBox;
