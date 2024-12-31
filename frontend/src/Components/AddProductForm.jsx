import { useEffect, useState } from "react";
import { toast } from "react-toastify";

const AddProductForm = () =>{
    const [formVariables, setFormVariables] = useState({state: 'ON_SALE', dateOfCreation: new Date().toISOString(), rate: 0.0, numberOfFeedback: 0});
    const [selectedCatigory, setSelectedCatigory] = useState("");
    const [addedCatigories, setAddedCatigories] = useState([]);
    const [loadedCatigories, setLoadedCatigories] = useState([]);
    const [numberOfVariants, setNumberOfVariants] = useState(1);
    const [variants, setVariants] = useState([{sold: 0, state: 'ON_SALE', discount: 0.0}]);
    useEffect(()=>{
        getCategories();
    }, []);


    const notifySuccess= (message) =>{
        toast.success(message);
    }

    const notifyFailier = (message) =>{
        toast.error(message);
    }

    const getCategories = async()=>{
        await fetch("http://localhost:8081/api/7/categories/", {
            method:'GET',
            headers:{
              'Content-Type': 'application/json',
               'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            }
          })
          .then(response=>response.status==200 || response.status==201?(()=>{return response.json()})():(()=>{throw Error("Error fetching catigories")})())
          .then (catigoriesData=>{setLoadedCatigories(catigoriesData)})
          .catch(e=>console.error(e));
    }

    const getListOfCatigories = (catigoriesNamesList)=>{
        let finalCatigoriesList = []
        catigoriesNamesList.forEach((catigoryName)=>{
            loadedCatigories.forEach((catigoryObj)=>{
                if (catigoryObj.name === catigoryName){
                    finalCatigoriesList.push(catigoryObj);
                }
            })
        })
        return finalCatigoriesList;
    }
    const addProduct = async(e)=>{
        e.preventDefault();
        //adding final catigories
        console.log(console.log({...formVariables, categories: getListOfCatigories(addedCatigories)}));

        await fetch(`http://localhost:8081/api/1000/shop/c/product`, {
            method:"POST",
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
            },
            body:JSON.stringify(
                {
                    ...formVariables, 
                    categories: getListOfCatigories(addedCatigories)
                }
            )
        })
        .then(response=>response.status==200 || response.status==201?(async()=>{
           
        })(): (()=>{throw Error("Error adding product")})())
        .catch(e=>{console.log(e); notifyFailier("Failed to add the product")})
    }

    const handleChange = (event, isVariant = false)=>{
        const {name, value} = event.target;
        if (isVariant) setFormVariables({...formVariables, variants: variants});
        else setFormVariables({...formVariables, [name]: value});
    }

    const handleAddCatigory = (event)=>{
        event.preventDefault();
        let checkFlag = false;
        if (selectedCatigory == "") checkFlag = true;
        if (!checkFlag){
            addedCatigories.forEach((catigory)=>{
                if (catigory === selectedCatigory) checkFlag = true;
            })
        }
        if (!checkFlag) setAddedCatigories((prev)=>[...prev, selectedCatigory])
    }

    const handleAddVariant = (e)=>{
        e.preventDefault();
        setNumberOfVariants((prev)=>prev+1);
        setVariants((prev)=>[...prev, {sold: 0, state: 'ON_SALE', discount: 0.0}]);
    }

    const handleClearVariant = (index)=>{
        setNumberOfVariants((prev)=>prev-1);
        setVariants((prev)=>{console.log(prev.filter((_, i)=>i!=index)); return prev.filter((_, i)=>i!=index)});
    }

    return(
        <>
        <center><h1>New Product</h1></center>
        <form onSubmit={addProduct} style={{justifyItems:"left"}}>
            <h2 style={{margin:"0"}}>Description</h2>
            <textarea name="description" placeholder="Enter description" style={{margin:"1rem", fontSize:"2rem"}} rows="5" cols="80" onChange={handleChange} required/>
            <h2 style={{margin:"0"}}> Select Catigories</h2>
            <div className="selectedCatigoriesContainer">
                {addedCatigories.map((catigory, key)=>(
                    <div key={key}>{catigory + " "}<button onClick={()=>setAddedCatigories((prev)=>prev.filter((eachCatigory)=>eachCatigory !== catigory))}>x</button></div>
                ))}
            </div>
            <div style={{display:"flex", justifyContent:"center", alignItems:"center"}}>
                <select value={selectedCatigory} onChange={(e)=>{setSelectedCatigory(e.target.value)}} style={{marginRight:"1rem"}}>
                    <option value="" disabled>Select a catigory</option>
                    {loadedCatigories.map((catigory, key)=>(
                        <option key={key} value={catigory.name}>{catigory.name}</option>
                    ))}
                </select>
                <button className="backButton" onClick={handleAddCatigory}>Set Catigory</button>
            </div>
            <h2 style={{margin:"0"}}> Select Variants (at least one)</h2>
            <div style={{display:"flex", width:"100%", overflowX:"scroll", border:"0.5px solid", borderRadius:"20px"}}>
                {Array.from({length: numberOfVariants}, (_, i)=>i+1).map((number)=>(
                    <div className="variantContainer" key={number-1}>
                        <center>Variant {number}</center>
                        <label style={{color:"white"}} htmlFor="color">Color</label>
                        <input type="text" name="color" value={variants[number-1].color?variants[number-1].color:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                        
                        <label style={{color:"white"}} htmlFor="weight">Weight (in gm)</label>
                        <input type="text" name="weight" value={variants[number-1].weight?variants[number-1].weight:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                    
                        <label style={{color:"white"}} htmlFor="length">Length (in cm)</label>
                        <input type="text" name="length" value={variants[number-1].length?variants[number-1].length:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                    
                        <label style={{color:"white"}} htmlFor="size">Size</label>
                        <input type="text" name="size" value={variants[number-1].size?variants[number-1].size:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                    
                        <label style={{color:"white"}} htmlFor="stock">Amount in Stock</label>
                        <input type="text" name="stock" value={variants[number-1].stock?variants[number-1].stock:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                    
                        <label style={{color:"white"}} htmlFor="price">Price</label>
                        <input type="text" name="price" value={variants[number-1].price?variants[number-1].price:""} onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = event.target.value; return prev});handleChange(event, true);}} required/>
                    
                        <label style={{color:"black"}} htmlFor="images">Images</label>
                        <input type="file" name="images" onChange={(event)=>{setVariants((prev)=>{prev[number-1][event.target.name] = [event.target.files[0]]; return prev});handleChange(event, true);}} required/>
                    
                        {number!=1 && <center><button className="backButton" onClick={(e)=>{e.preventDefault();handleClearVariant(number-1);}}>Remove</button></center>}
                    </div>
                ))
                }
            </div>
            <button className="backButton" onClick={handleAddVariant} style={{width:"202%", backgroundColor:"#c15b658a"}}>Add variant</button><br/>
            <button className="backButton" type="submit" style={{width:"100vh", transform:"translate(50%, 0)"}}>Add Product</button>
        </form>
        </>
    );
} 

export default AddProductForm;