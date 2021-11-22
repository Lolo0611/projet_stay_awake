//Access the router on Express 
const router = require('express').Router();

//Access the controllers
const controller = require('../controllers/task');

//CREATE
router.post("/createTask", (req, res) => {

    controller.create(req, res);

});

//READ
router.get("/Tasks", (req, res) => {
    
    controller.reads(req, res);

});

router.get("/Task/:id", (req, res) => {
    
    controller.read(req, res);

});

//UPDATE
router.put("/updateTask/:id", (req, res) => {
    
    controller.updatePizza(req, res);

});

//DELETE
router.delete("/deleteTask/:id", (req, res) => {
    
    controller.delete(req, res);

});


module.exports = router;