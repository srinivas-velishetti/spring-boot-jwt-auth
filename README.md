# Spring Boot JWT Authentication Project

# Postman Setup with Authentication & Dynamic Host (DEV & SIT)

This README provides instructions to set up **Postman environments** and use the included **collection** with dynamic host and token management.  
It covers creating **DEV** and **SIT** environments, saving authentication tokens into environment variables, and reusing them in API requests.

---

## 🔹 1. Create Environments

You need two environments: **DEV** and **SIT**.

### DEV Environment
| Variable   | Example Value     | Description                                |
|------------|-------------------|--------------------------------------------|
| `host`     | `localhost:8080` | Host and port for the DEV environment      |
| `authToken`| *(leave blank)*  | This will be auto-populated after login    |

### SIT Environment
| Variable   | Example Value            | Description                               |
|------------|--------------------------|-------------------------------------------|
| `host`     | `sit`  | Host and port for the SIT environment     |
| `authToken`| *(leave blank)*         | This will be auto-populated after login   |

💡 To create:
1. Open Postman → **⚙️ Manage Environments** → **Add Environment**.  
2. Enter the variables as above for `DEV` and `SIT`.  
3. Save and select the environment from the dropdown (top-right in Postman).

---

## 🔹 2. Import Collection

Use the following Postman collection JSON:

<details>
<summary>Click to expand JSON</summary>

```json
{
	"info": {
		"_postman_id": "b6452753-ad6e-4536-a1ee-f0bc9ffc5c89",
		"name": "My Collection",
		"description": "Sample Postman Collection with login, signup, and get data requests using environment variables.",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "Get data",
			"event": [
				{
					"listen": "prerequest",
					"script": {
						"exec": [
							"// Call the login/auth endpoint before running this request",
							"let host = pm.environment.get(\"host\");",
							"console.log(\"Saved host: \" + host);",
							"pm.sendRequest({",
							"    url: \"http://\"+host+\"/auth/login\",",
							"    method: \"POST\",",
							"    header: {",
							"        \"Content-Type\": \"application/json\"",
							"    },",
							"    body: {",
							"        mode: \"raw\",",
							"        raw: JSON.stringify({",
							"            username: \"Srini\",",
							"            password: \"12345\"",
							"        })",
							"    }",
							"}, function (err, res) {",
							"    if (err) {",
							"        console.log(\"Login request failed: \" + err);",
							"    } else {",
							"        let data = res.json();",
							"        if (data.token) {",
							"            pm.environment.set(\"authToken\", data.token);",
							"            console.log(\"Saved token: \" + data.token);",
							"        } else if (data.access_token) {",
							"            pm.environment.set(\"authToken\", data.access_token);",
							"            console.log(\"Saved access_token: \" + data.access_token);",
							"        } else {",
							"            console.log(\"No token found in login response\");",
							"        }",
							"    }",
							"});"
						],
						"type": "text/javascript"
					}
				}
			],
			"request": {
				"method": "GET",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json",
						"type": "text"
					},
					{
						"key": "Authorization",
						"value": "Bearer {{authToken}}",
						"type": "text"
					}
				],
				"url": {
					"raw": "http://{{host}}/users/me",
					"protocol": "http",
					"host": [
						"{{host}}"
					],
					"path": [
						"users",
						"me"
					]
				},
				"description": "GET request to fetch user data using authToken."
			},
			"response": []
		},
		{
			"name": "login",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\n    \"username\": \"Srini\",\n    \"password\": \"123451\"\n}"
				},
				"url": {
					"raw": "http://{{host}}/auth/login",
					"protocol": "http",
					"host": [
						"{{host}}"
					],
					"path": [
						"auth",
						"login"
					]
				},
				"description": "POST request to authenticate and fetch token."
			},
			"response": []
		},
		{
			"name": "signup",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\n    \"username\": \"Srini\",\n    \"password\": \"12345\"\n}"
				},
				"url": {
					"raw": "http://{{host}}/auth/signup",
					"protocol": "http",
					"host": [
						"{{host}}"
					],
					"path": [
						"auth",
						"signup"
					]
				},
				"description": "POST request to register a new user."
			},
			"response": []
		}
	]
}
