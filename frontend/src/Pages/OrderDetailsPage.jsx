import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './OrderDetailsPage.css'; // Assuming you have a CSS file for styling
import { toast } from 'react-toastify';
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "../Components/ObjectToAppear";

const OrderDetailsPage = () => {
  const { orderId } = useParams();
  const [orderDetails, setOrderDetails] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    fetchOrderDetails();
  }, [orderId]);

  const fetchOrderDetails = async () => {
    try {
      const response = await fetch(`http://localhost:8081/orders/order-details/${orderId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      const data = await response.json();
      console.log(data);
      setOrderDetails(data);
    } catch (error) {
      console.error('Error fetching order details:', error);
    }
  };

  const handleChangeStatus = async () => {
    setIsLoading(()=>true);
    let endpoint = '';
    if (orderDetails.meta.status === 'PENDING') {
      endpoint = 'approve';
    } else if (orderDetails.meta.status === 'APPROVED') {
      endpoint = 'deliver';
    } else if (orderDetails.meta.status === 'DELIVERY') {
      endpoint = 'confirm';
    }

    try {
      await fetch(`http://localhost:8081/orders/${endpoint}/${orderId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      toast.success(`Status of the order of id: ${orderId} changed into ${endpoint} successfully`)
    } catch (error) {
      console.error(`Error changing order status to ${endpoint}:`, error);
      toast.success(`Failed to change the status of the order of id: ${orderId} to ${endpoint}`)
    }
    setIsLoading(()=>false);
    location.reload();
  };

  if (!orderDetails) {
    return <div>Loading...</div>;
  }

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  let buttonText = '';
  if (orderDetails.meta.status === 'PENDING') {
    buttonText = 'APPROVE';
  } else if (orderDetails.meta.status === 'APPROVED') {
    buttonText = 'DELIVER';
  } else if (orderDetails.meta.status === 'DELIVERY') {
    buttonText = 'CONFIRM';
  }

  return (
    <div className="order-details-page">
      <h1>Order Details</h1>
      <div className="order-info">
        <p><strong>Total Price:</strong> {orderDetails.meta.totalPrice}</p>
        <p><strong>Status:</strong> {orderDetails.meta.status}</p>
        <p><strong>Time:</strong> {formatDate(orderDetails.meta.timeStamp)}</p>
        <p><strong>Address:</strong> {orderDetails.meta.address}</p>
        <p><strong>Customer Name:</strong> {orderDetails.meta.customerName}</p>
      </div>
      <h2>Items</h2>
      <table className="order-items-table">
        <thead>
          <tr>
            <th>Product Name</th>
            <th>Item Total Price</th>
            <th>Color</th>
            <th>Price</th>
            <th>Size</th>
            <th>Quantity</th>
          </tr>
        </thead>
        <tbody>
          {orderDetails.items.map(item => (
            <tr key={item.productName} style={{cursor:"pointer"}}>
              <td>{item.productName}</td>
              <td>{item.itemTotalPrice}</td>
              <td>{item.productVariantColor}</td>
              <td>{item.productVariantPrice}</td>
              <td>{item.productVariantSize}</td>
              <td>{item.productVariantQuantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {buttonText && (
        <div>
        <button onClick={handleChangeStatus} className="status-button">
          {buttonText}
        </button>
        {isLoading && 
        <AnimatePresence>
          <ObjectToAppear size={10}/>
        </AnimatePresence>}
        </div>
      )}
    </div>
  );
};

export default OrderDetailsPage;
