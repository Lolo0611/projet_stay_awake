var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// Location point model
const pointSchema = new mongoose.Schema({
  type: {
    type: String,
    enum: ['Point'],
    required: true
  },
  coordinates: {
    type: [Number],
    required: true
  }
});

const TaskSchema = new Schema({
  title : String,
  startDate : Date,
  endDate : Date,
  location: pointSchema,
  description: String,
  priority : Number
});

module.exports = mongoose.model('Task', TaskSchema);