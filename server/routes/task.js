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

router.get("/TasksByDay/:day", (req, res) => {

    controller.readsByDay(req, res);

})

router.get("/Task/:id", (req, res) => {
    
    controller.read(req, res);

});

//UPDATE
router.put("/updateTask/:id", (req, res) => {
    
    controller.update(req, res);

});

router.put("/checkedTask/:id", (req, res) => {
    
    controller.checked(req, res);

});

//DELETE
router.delete("/deleteTask/:id", (req, res) => {
    
    controller.delete(req, res);

});


module.exports = router;