const dayjs = require('dayjs')
const customParseFormat = require('dayjs/plugin/customParseFormat')
const isBetween = require('dayjs/plugin/isBetween')

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

module.exports.cleanTasks = cleanTasks
module.exports.checkIfTaskAlreadyInPosition = checkIfTaskAlreadyInPosition