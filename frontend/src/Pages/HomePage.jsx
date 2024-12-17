import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";

const ITEMS_PER_PAGE = 10;

const HomePage = () => {
  const navigate = useNavigate();
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [openCategories, setOpenCategories] = useState({});
  const [products, setProducts] = useState([]);
  const [categoryTree, setCategoryTree] = useState({}); // Category structure from API
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);

  // Function to fetch all products
  const fetchAllProducts = async (page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/1000/shop/products?page=${page - 1}&size=${ITEMS_PER_PAGE}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) throw new Error("Error fetching all products");

      const data = await response.json();
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalItems / ITEMS_PER_PAGE));
    } catch (error) {
      console.error("Error fetching all products:", error);
    }
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/7/categories/${category}?page=${page - 1}&size=${ITEMS_PER_PAGE}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) throw new Error(`Error fetching products for category ${category}`);

      const data = await response.json();

      // Extract products and handle subcategories
      const products = data.products || [];
      setProducts(products);

      // Calculate total pages based on total items (assuming `data.totalItems` is provided)
      setTotalPages(Math.ceil((data.totalItems || products.length) / ITEMS_PER_PAGE));
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };


  // Function to fetch categories from the API
  const fetchCategories = async () => {
    try {
      setIsLoadingCategories(true);
      const response = await fetch("http://localhost:8081/api/7/categories/");
      if (!response.ok) throw new Error("Failed to fetch categories");

      const data = await response.json();

      // Extract the category tree from the response
      const tree = data.tree || {}; // Assuming `tree` is the key containing the category tree
      setCategoryTree(tree);

      setActiveCategory("All"); // Default to "All" products
    } catch (error) {
      console.error("Error fetching category data:", error);
    } finally {
      setIsLoadingCategories(false);
    }
  };

  useEffect(() => {
    // Fetch categories on initial render
    fetchCategories();
  }, []);

  useEffect(() => {
    // Fetch products based on the active category
    if (activeCategory === "All") {
      fetchAllProducts(currentPage);
    } else {
      fetchCategoryProducts(activeCategory, currentPage);
    }
  }, [currentPage, activeCategory]);

  const toggleCategory = (mainCategory) => {
    setOpenCategories((prev) => ({
      ...prev,
      [mainCategory]: !prev[mainCategory],
    }));
  };

  const handleCategoryClick = (category) => {
    setActiveCategory(category);
    setCurrentPage(1); // Reset to the first page when the category changes
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      <div className="sidebar">
        <h3>Categories</h3>
        {isLoadingCategories ? (
          <p>Loading categories...</p>
        ) : (
          Object.keys(categoryTree).map((mainCategory) => (
            <div key={mainCategory} className="category-group">
              <h4
                className="collapsible-header"
                onClick={() => toggleCategory(mainCategory)}
              >
                {mainCategory} {openCategories[mainCategory] ? "-" : "+"}
              </h4>
              {openCategories[mainCategory] && (
                <ul>
                  {categoryTree[mainCategory].map((subcategory) => (
                    <li
                      key={subcategory}
                      className={`category ${
                        activeCategory === subcategory ? "active" : ""
                      }`}
                      onClick={() => handleCategoryClick(subcategory)}
                    >
                      {subcategory}
                    </li>
                  ))}
                </ul>
              )}
            </div>
          ))
        )}
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
            <div
              key={product.productID}
              className="product-card"
              onClick={() => navigate(`product/${product.productID}`)}
            >
              {/* Display image */}
              <img
                src={product.productImage}
                alt={product.description}
                className="product-image"
              />
              <div className="product-details">
                <p className="price">{product.price}</p>
                <p>{product.description}</p>
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
