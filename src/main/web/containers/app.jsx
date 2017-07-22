import React from 'react';
import {Tab, Tabs} from 'react-bootstrap';
import Overview from './overview';
import CashFlow from './cashFlow';

export default function app() {
	return (
		<Tabs defaultActiveKey={2} id="cashbook-tabs" animation>
			<Tab eventKey={1} title="Overview">
				<Overview/>
			</Tab>
			<Tab eventKey={2} title="Cash flow">
				<CashFlow/>
			</Tab>
			<Tab eventKey={3} title="Categories">

			</Tab>
		</Tabs>
	);
}
