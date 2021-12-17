const dayjs = require('dayjs')
const customParseFormat = require('dayjs/plugin/customParseFormat')
const isBetween = require('dayjs/plugin/isBetween')
const { checked } = require('../controllers/task')

dayjs.extend(customParseFormat)
dayjs.extend(isBetween)

function cleanTasks (tasks) {

    let Task = require("../models/task");

    tasks.forEach(task => {
        if(task.day != undefined && (dayjs().isAfter(dayjs(task.day, "YYYY-MM-DD"), 'day'))) {
            task.day = undefined
            task.hour = undefined
            task.duration = undefined

            Task.findByIdAndUpdate(
                {_id: task.id}, 
                {day : undefined, hour : undefined, duration : undefined},
                {new : false})
        }
    });

    return tasks
}

function checkIfTaskAlreadyInPosition(tasks, day, hour, duration) {
    let isCorrect = true
    let hourToDate = dayjs(hour, "HH:mm")

    tasks.forEach(task => {
        if(task.day === day && !task.checked) {
            const taskHour = dayjs(task.hour, "HH:mm")
            if(hourToDate.isBetween(taskHour, taskHour.add(task.duration, 'hour'), '[]')) {
                isCorrect = false
            } else if(hourToDate.add(duration, 'hour').isBetween(taskHour, taskHour.add(task.duration, 'hour'), '[]')) {
                isCorrect = false
            }
        }
    })

    return isCorrect
}

function checkIfTaskIsNear(task) {
    isNear = false
    if(dayjs(task.hour, "HH:mm").isBetween(dayjs(), dayjs().add(1, "hour"))) {
        isNear = true
    }

    return isNear;
}

function updatePermanentDay(tasks, requestedDay) {
    let Task = require("../models/task");

    tasksToRemove = []

    tasks.forEach(task => {
        if(task.permanent) {
            if(task.checked && (dayjs().isAfter(dayjs(task.day, "YYYY-MM-DD"), 'day'))) {
                task.checked = false

                Task.findByIdAndUpdate(
                    {_id: task.id}, 
                    {checked : false},
                    {new : false})
            } 

            if(task.day && dayjs(task.day).day() === dayjs(requestedDay).day()){
                task.day = requestedDay

                if(task.day !== dayjs().format("YYYY-MM-DD")) {
                    task.checked = false
                }

                Task.findByIdAndUpdate(
                    {_id: task.id}, 
                    {day : requestedDay},
                    {new : false})
            } else {
                tasksToRemove.push(task)
            }
        }
    })

    tasksToRemove.forEach(task => {
        tasks.splice(tasks.indexOf(task), 1)
    })

    return tasks
}

module.exports.cleanTasks = cleanTasks
module.exports.checkIfTaskAlreadyInPosition = checkIfTaskAlreadyInPosition
module.exports.checkIfTaskIsNear = checkIfTaskIsNear
module.exports.updatePermanentDay = updatePermanentDay