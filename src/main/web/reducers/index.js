import { combineReducers } from 'redux'

import cashFlowReducer from './cashFlowReducer';
import filterReducer from './filterReducer';

export default combineReducers({
	cashFlow: cashFlowReducer,
	filter: filterReducer
});
