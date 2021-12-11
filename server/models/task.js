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
  day : String,
  hour : String,
  duration : Number,
  location: pointSchema,
  description: String,
  priority : Number,
  checked : Boolean
});

module.exports = mongoose.model('Task', TaskSchema);