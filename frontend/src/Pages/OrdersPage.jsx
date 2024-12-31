import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './OrdersPage.css'; // Assuming you have a CSS file for styling

const OrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(2);
  const [status, setStatus] = useState('ALL');
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    fetchOrders();
  }, [page, size, status]);

  const fetchOrders = async () => {
    try {
      const queryParams = new URLSearchParams({
        page: (page - 1).toString(),
        size: size.toString(),
      });

      if (status !== 'ALL') {
        queryParams.append('status', status);
      }

      const response = await fetch(`http://localhost:8081/orders?${queryParams.toString()}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      const data = await response.json();
      console.log(data);
      setOrders(data.data);
      setTotalPages(data.meta.totalPages);
      
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const handleApprove = async (orderId) => {
    try {
      await fetch(`http://localhost:8081/orders/approve/${orderId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      fetchOrders();
    } catch (error) {
      console.error('Error approving order:', error);
    }
  };

  const handleDeliver = async (orderId) => {
    try {
      await fetch(`http://localhost:8081/orders/deliver/${orderId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      fetchOrders();
    } catch (error) {
      console.error('Error delivering order:', error);
    }
  };

  const handleConfirm = async (orderId) => {
    try {
      await fetch(`http://localhost:8081/orders/confirm/${orderId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      fetchOrders();
    } catch (error) {
      console.error('Error confirming order:', error);
    }
  };

  const handleCancel = async (orderId) => {
    try {
      await fetch(`http://localhost:8081/orders/cancel-order/${orderId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('drip_me_up_jwt')}`,
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      fetchOrders();
    } catch (error) {
      console.error('Error canceling order:', error);
    }
  };

  const handleOrderClick = (orderId) => {
    navigate(`/adminSession/orders/${orderId}`);
  };
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  return (
    <div className="orders-page">
      <h1>Orders</h1>
      <div className="filter-controls">
        <label htmlFor="status">Filter by Status:</label>
        <select id="status" value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="ALL">ALL</option>
          <option value="PENDING">PENDING</option>
          <option value="APPROVED">APPROVED</option>
          <option value="DELIVERY">DELIVERY</option>
          <option value="CONFIRMED">CONFIRMED</option>
        </select>
      </div>
      <table className="orders-table">
        <thead>
          <tr>
            {/* <th>ID</th> */}
            <th>Total Price</th>
            <th>Time</th>
            <th>Address</th>
            <th>Status</th>
            {/* <th>Actions</th> */}
          </tr>
        </thead>
        <tbody>
          {orders.map(order => (
            <tr key={order.id} onClick={() => handleOrderClick(order.id)}>
              {/* <td>{order.id}</td> */}
              <td>{order.totalPrice}</td>
              <td>{formatDate(order.timeStamp)}</td>
              <td>{order.address}</td>
              <td>{order.status}</td>
              {/* <td>
                <button onClick={() => handleApprove(order.id)}>Approve</button>
                <button onClick={() => handleDeliver(order.id)}>Deliver</button>
                <button onClick={() => handleConfirm(order.id)}>Confirm</button>
                <button onClick={() => handleCancel(order.id)}>Cancel</button>
              </td> */}
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination-controls">
        <button disabled={page <= 1} onClick={() => setPage(page - 1)}>Previous</button>
        <span>Page {page} of {totalPages}</span>
        <button disabled={page >= totalPages} onClick={() => setPage(page + 1)}>Next</button>
      </div>
    </div>
  );
};

export default OrdersPage;
