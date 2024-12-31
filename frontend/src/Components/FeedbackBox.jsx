import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css'; // Import toast styles
import unknownPhoto from '../assets/unknown.jpg'; // Adjust the path as necessary

const FeedbackBox = ({ feedback }) => {
    const [userProfile, setUserProfile] = useState(null);
    const [newFeedback, setNewFeedback] = useState('');
    const [editingFeedback, setEditingFeedback] = useState(null); // Track feedback being edited
    const token = localStorage.getItem('drip_me_up_jwt');

    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const currentUserId = decodedToken.userId || decodedToken.id || decodedToken.sub;

    const handleEditFeedback = (feedback) => {
        setEditingFeedback(feedback); // Start editing this feedback
        setNewFeedback(feedback.feedback); // Pre-fill textarea with the current feedback
    };
    

    const handleUpdateFeedback = async () => {
        if (!editingFeedback) return;
    
        try {
            const response = await fetch(`http://localhost:8081/api/feedback/${editingFeedback.feedbackId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
                },
                body: JSON.stringify({
                    feedback: newFeedback,                    
                    productId: feedback.productId,
                    userId: feedback.userId,
                }),
            });
    
            if (response.ok) {
                const updatedFeedback = await response.json();
                console.log(updatedFeedback);
                setEditingFeedback(null); // Exit edit mode
                setNewFeedback(''); // Clear the input
                toast.success('Feedback updated successfully!');
                window.location.reload(); // Refresh the page to show the updated feedback
            } else {
                toast.error('Failed to update feedback.');
            }
        } catch (error) {
            console.error('Error updating feedback:', error);
            toast.error('An error occurred while updating feedback.');
        }
    };
    

    const handleDeleteFeedback = async (feedback) => {
        try {

            const response = await fetch(`http://localhost:8081/api/feedback/${feedback.feedbackId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
                },
            });

            if (response.ok) {
                // setFeedbacks(feedbacks.filter(f => f.id !== feedback.feedback_id));
                toast.success('Feedback deleted successfully!');
                //refresh
                window.location.reload();

            } else {
                toast.error('Failed to delete feedback.');
            }
        } catch (error) {
            console.error('Error deleting feedback:', error);
            toast.error('An error occurred while deleting feedback.');
        }
    };

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await fetch(`http://localhost:8081/users/${feedback.userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (response.ok) {
                    const userData = await response.json();
                    setUserProfile(userData);
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
            fetchUserData();
        }
    }, [feedback.userId]);

    return (
        <div className='feedbackBox'>
            <img
                className="reviewPhoto"
                src={userProfile?.profilePhoto || unknownPhoto}
                alt='UserPhoto'
            />
            <div className='feedbackData'>
                <p style={{ margin: '0' }}>{userProfile?.username || 'Unknown User'}</p>
                <p style={{ margin: '0' }}>{feedback.feedback}</p>
                <p style={{ margin: '0', fontSize: '1rem' }}>{feedback.time}</p>
                {currentUserId === feedback.userId && (
                    <div style={{ marginTop: '10px' }}>
                        {editingFeedback && editingFeedback.id === feedback.id ? (
                            <div>
                                <textarea
                                    value={newFeedback}
                                    onChange={(e) => setNewFeedback(e.target.value)}
                                    style={{ width: '100%', height: '50px', marginBottom: '10px' }}
                                />
                                <button
                                    onClick={handleUpdateFeedback}
                                    style={{
                                        marginRight: '5px',
                                        padding: '5px 10px',
                                        borderRadius: '3px',
                                        background: '#28a745',
                                        color: '#fff',
                                        border: 'none',
                                    }}
                                >
                                    Save
                                </button>
                                <button
                                    onClick={() => setEditingFeedback(null)}
                                    style={{
                                        padding: '5px 10px',
                                        borderRadius: '3px',
                                        background: '#dc3545',
                                        color: '#fff',
                                        border: 'none',
                                    }}
                                >
                                    Cancel
                                </button>
                            </div>
                        ) : (
                            <button
                                onClick={() => handleEditFeedback(feedback)}
                                style={{
                                    marginRight: '5px',
                                    padding: '5px 10px',
                                    borderRadius: '3px',
                                    background: '#007bff',
                                    color: '#fff',
                                    border: 'none',
                                }}
                            >
                                Edit
                            </button>
                        )}
                        <button
                            onClick={() => handleDeleteFeedback(feedback)}
                            style={{
                                padding: '5px 10px',
                                borderRadius: '3px',
                                background: '#dc3545',
                                color: '#fff',
                                border: 'none',
                            }}
                        >
                            Delete
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FeedbackBox;
