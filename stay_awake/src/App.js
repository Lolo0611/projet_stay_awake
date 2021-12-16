import './App.css';
import TaskPanel from './components/TaskPanel';
import Navbar from './components/Navbar';
import Agenda from './components/Agenda';
import Board from "./components/Board";
import Card from "./components/Card";
import Task from './components/Task';

function App() {
    return ( 
        <div className = "App" >
        <div className = "App-header" >
        <h1> Stay Awake </h1>  
        </div>

        <div className = "content" >

            <div className = "NavBar" >
            <Navbar/>
            </div>

            <div className = "content-right">
            <div className = "agenda" >
            <Agenda/>   
            
            </div>

        <TaskPanel/>

            </div>
        </div> 
        </div>
        
    );
}

export default App;