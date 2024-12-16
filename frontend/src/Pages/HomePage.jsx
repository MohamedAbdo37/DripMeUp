import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "../homepage.css";

const ITEMS_PER_PAGE = 10;

const HomePage = () => {
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1); // Track the total number of pages
  const [openCategories, setOpenCategories] = useState({});
  const [products, setProducts] = useState([]);
  const [categoryTree, setCategoryTree] = useState({
    Men: ["T-Shirts", "Shirts", "Jackets", "Trousers", "Shorts", "Sweatshirts"],
    Women: ["T-Shirts", "Shirts", "Jackets", "Trousers", "Shorts", "Sweatshirts"],
  });

  // Function to fetch all products (for default category)
  const fetchAllProducts = async (page = 1) => {
    try {
      const response = await axios.get(`http://localhost:8081/api/1000/shop/products`, {
        params: {
          page,
        },
      });
      setProducts(response.data.products);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Error fetching all products:", error);
    }
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await axios.get(`/api/products/bycategory/${category}`, {
        params: {
          page,
        },
      });
      setProducts(response.data.products);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };

  useEffect(() => {
    // Fetch products based on the active category
    if (activeCategory === "All") {
      fetchAllProducts(currentPage); // Fetch all products if category is "All"
    } else {
      fetchCategoryProducts(activeCategory.toLowerCase().replace(" ", ""), currentPage); // Fetch category products
    }
  }, [currentPage, activeCategory]); // Re-fetch when currentPage or activeCategory changes

  const toggleCategory = (gender) => {
    setOpenCategories((prev) => ({
      ...prev,
      [gender]: !prev[gender],
    }));
  };

  const handleCategoryClick = (category) => {
    setActiveCategory(category);
    setCurrentPage(1); // Reset to page 1 when category changes
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      <div className="sidebar">
        <h3>Categories</h3>
        {Object.keys(categoryTree).map((gender) => (
          <div key={gender} className="category-group">
            <h4
              className="collapsible-header"
              onClick={() => toggleCategory(gender)}
            >
              {gender} {openCategories[gender] ? "-" : "+"}
            </h4>
            {openCategories[gender] && (
              <ul>
                {categoryTree[gender].map((category) => (
                  <li
                    key={category}
                    className={`category ${
                      activeCategory === category ? "active" : ""
                    }`}
                    onClick={() => handleCategoryClick(category)}
                  >
                    {category}
                  </li>
                ))}
              </ul>
            )}
          </div>
        ))}
        <li
          className={`category ${activeCategory === "All" ? "active" : ""}`}
          onClick={() => handleCategoryClick("All")}
        >
          Show All
        </li>
      </div>

      {/* Product Grid */}
      <div className="content">
        <div className="product-grid">
          {products.map((product) => (
            <div key={product.id} className="product-card">
              <Link to={`/product/${product.id}`}>
                {/* Display image */}
                <img src={product.image} alt={product.name} className="product-image" />
              </Link>
              <div className="product-details">
                {/* Display price */}
                <p className="price">{product.price}</p>
                {/* Display product name */}
                <p>{product.name}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        <div className="pagination">
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i + 1}
              className={`page-btn ${currentPage === i + 1 ? "active" : ""}`}
              onClick={() => handlePageChange(i + 1)}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
