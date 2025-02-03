import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";
import { AnimatePresence } from "framer-motion";
import ObjectToAppear from "../Components/ObjectToAppear";

const ITEMS_PER_PAGE = 10;

const HomePage = () => {
  const navigate = useNavigate();
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [openCategories, setOpenCategories] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [products, setProducts] = useState([
    
    // More products...
  ]);
  const [categoryTree, setCategoryTree] = useState({
    
  }); // Category structure from API
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);
  const mapCategoryTree = (categories) => {
    const categoryMap = {};
  
    // Create a map of categories by name
    categories.forEach(category => {
      categoryMap[category.name] = {
        id: category.id,
        name: category.name,
        description: category.description,
        subcategories: []
      };
    });
  
    // Build the tree structure
    categories.forEach(category => {
      category.subcategoryNames.forEach(subcategoryName => {
        if (categoryMap[subcategoryName]) {
          categoryMap[category.name].subcategories.push(categoryMap[subcategoryName]);
        }
      });
    });
  
    // Find the root categories (those that are not subcategories of any other category)
    const rootCategories = categories.filter(category => {
      return !categories.some(cat => cat.subcategoryNames.includes(category.name));
    });
  
    // Create the "All Categories" wrapper
    const allCategoriesWrapper = {
      id: 0,
      name: "All Categories",
      subcategories: rootCategories.map(category => categoryMap[category.name])
    };
  
    // Return the tree structure with the "All Categories" wrapper
    return allCategoriesWrapper;
  };
  
  // Example usage
  const backendCategories = [
  ];
  
  console.log(categoryTree);
  // Function to fetch all products
  const fetchAllProducts = async (page = 1) => {
    setIsLoading(true);
    const productsFetched = await fetch(`http://localhost:8081/api/1000/shop/products?page=${page-1}&size=${10}`, {
      method:'GET',
      headers:{
        'Content-Type': 'application/json',
         'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
      }
    })
    .then(response=>response.status==200 || response.status==201?(()=>{return response.json()})():(()=>{throw Error("Error fetching all products")})())
    .then(data=>{
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalPages));
    })
    setIsLoading(false);
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/1000/shop/category?category=${category}&page=${page - 1}&size=${ITEMS_PER_PAGE}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
          },
        }
      );
      if (!response.ok) {
        console.log(`Error fetching products for category ${category}`);
        setProducts([]);
      }
      else{
        const data = await response.json();

        // Extract products and handle subcategories
        const products = data.content;
        setProducts(products);

        // Calculate total pages based on total items (assuming `data.totalItems` is provided)
        setTotalPages(Math.ceil((data.totalItems || products.length) / ITEMS_PER_PAGE));
      }

    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };


  // Function to fetch categories from the API
  const fetchCategories = async () => {
    try {
      setIsLoadingCategories(true);
      const response = await fetch("http://localhost:8081/api/7/categories/", {
        method:'GET',
        headers:{
          'Content-Type': 'application/json',
           'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
        }
      });
      if (!response.ok) throw new Error("Failed to fetch categories");

      const data = await response.json();

      // Extract the category tree from the response
      const nodeTree = data || {}; // Assuming `tree` is the key containing the category tree
      console.log("waiting for the tree...1");
      console.log(nodeTree);
      const nestedTree = mapCategoryTree(nodeTree)
      console.log("waiting for the tree...2");
      console.log(nestedTree);
      setCategoryTree(nestedTree);
      console.log("waiting for the tree...3");
      console.log(nestedTree);
      setActiveCategory("All"); // Default to "All" products
    } catch (error) {
      console.error("Error fetching category data:", error);
    } finally {
      setIsLoadingCategories(false);
    }
  };


  useEffect(()=>{
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
  const expandAllCategories = (category) => {
    const openCategories = {};
    const traverse = (node) => {
      openCategories[node.id] = true;
      if (node.subcategories) {
        node.subcategories.forEach(traverse);
      }
    };
    traverse(category);
    setOpenCategories(openCategories);
  };

  const renderCategoryTree = (category, level = 0) => {
    if (!category) return null;

    return (
      <ul key={category.id} className={`level-${level}`}>
        <li>
          <div className="category-item" onClick={() => handleCategoryClick(category.name)}>
            <span>{category.name}</span>
            {category.subcategories && category.subcategories.length > 0 && (
              <span className="toggle" onClick={(e) => { e.stopPropagation(); toggleCategory(category.id); }}>
                {openCategories[category.id] ? "-" : "+"}
              </span>
            )}
          </div>
          {openCategories[category.id] && (
            <ul>
              {category.subcategories.map((subcategory) => renderCategoryTree(subcategory, level + 1))}
            </ul>
          )}
        </li>
      </ul>
    );
  };

  const handleRenderStaticTree = () => {
    const treee = mapCategoryTree(backendCategories);
    console.log(treee);
    setCategoryTree(treee);
    //expandAllCategories(staticTree);
  };
  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      <div className="sidebar">
        <h3>Categories</h3>
        {/* <button onClick={handleRenderStaticTree}>Render Static Tree</button> */}
        <li
          className={`category ${activeCategory === "All" ? "active" : ""}`}
          onClick={() => {
            handleCategoryClick("All");
            expandAllCategories(categoryTree);
          }}
        >
          Show All
        </li>
        {isLoadingCategories ? (
          <p>Loading categories...</p>
        ) : (
          categoryTree && renderCategoryTree(categoryTree)
        )}
      </div>
      {/* Product Grid */}
      <div className="content">
        <div className="product-grid">
          {isLoading?<center><AnimatePresence>
            <ObjectToAppear size={100}/>
        </AnimatePresence></center>:products.map((product) => (
            <div key={product.productID} className="product-card" onClick={()=>navigate(`/userSession/product/user/${product.productID}/0`)}>
                {/* Display image */}
                <img src={product.productImage} alt="productImage" className="product-image" />
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
