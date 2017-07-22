import {
	RECEIVE_CASH_FLOW,
	REQUEST_CASH_FLOW
} from '../actions/cashFlowActions';

function cashFlow(state = {isFetching : false, flow : []}, action) {
	switch (action.type) {
		case REQUEST_CASH_FLOW:
			return Object.assign({}, state, {
				isFetching : true,
			});
		case RECEIVE_CASH_FLOW:
			return Object.assign({}, state, {
				isFetching : false,
				flow : action.flow
			});
		default:
			return state;
	}
}

export default function cashFlowReducer(state = {}, action) {
	switch (action.type) {
		case REQUEST_CASH_FLOW:
		case RECEIVE_CASH_FLOW:
			return Object.assign({}, state, {
				[action.filter] : cashFlow(state[action.filter], action)
			});
		default:
			return state
	}
}