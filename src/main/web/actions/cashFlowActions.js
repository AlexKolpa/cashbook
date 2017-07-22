import fetch from 'isomorphic-fetch'

export const REQUEST_CASH_FLOW = 'REQUEST_CASH_FLOW';
export const RECEIVE_CASH_FLOW = 'RECEIVE_CASH_FLOW';

function requestCashFlow(filter) {
	return {
		type : REQUEST_CASH_FLOW,
		filter
	}
}

function receiveCashFlow(flow, filter) {
	return {
		type : RECEIVE_CASH_FLOW,
		flow,
		filter
	}
}

function fetchCashFlows(filter) {
	return async (dispatch) => {
		dispatch(requestCashFlow(filter));
		const result = await fetch(`http://localhost:8080/flows?interval=${filter.interval}&index=${filter.index}`);
		const json = result.json();
		dispatch(receiveCashFlow(json, filter));
	}
}

function shouldFetchCashFlow(state, filter) {
	const cashFlow = state.cashFlow[filter];
	return !cashFlow || !cashFlow.isFetching;
}

export function fetchCashFlowIfNeeded(filter) {
	return (dispatch, getState) => {
		if (shouldFetchCashFlow(getState(), filter)) {
			return dispatch(fetchCashFlows(filter));
		}
	}
}
