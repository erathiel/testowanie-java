Given a manager
When create flower with name Roza and add to manager
Then size of manager should be 1

When create flower with name Fiolek and add to manager
Then name of flower in manager should be Fiolek

When delete flower with name Fiolek
Then only flower with name Roza should remain in manager and its size should be 1