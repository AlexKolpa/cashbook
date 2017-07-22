import {
	UPDATE_FILTER
} from '../actions/filterActions';

export default function filter(state = {}, action) {
	switch (action.type) {
		case UPDATE_FILTER:
			return Object.assign({}, state, {
				filter : Object.assign({interval : 'monthly', index : 0}, state.filter, action.filter)
			});
		default:
			return state;
	}
}
