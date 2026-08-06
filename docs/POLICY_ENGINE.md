# Policy Engine Specification

The Policy Engine checks request payloads against safety rules.

## Rule Structure
A Policy is composed of multiple `PolicyRule` records evaluated by order of priority.

## Supported Condition Types
1. **contains("keyword")**: Substring match inside prompt prompts.
2. **matches("regex")**: Full regex validation.
3. **key == "value"**: Matches parameters inside payload maps.
