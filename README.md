# Cart Application in Kotlin

This is a practice kotlin cart application.

following routes are supported

GET request for fetching cart
```curl
curl --location 'http://0.0.0.0:8080/cart'
```

POST request for adding items
```declarative
curl --location 'http://0.0.0.0:8080/cart/add' \
--header 'Content-Type: application/json' \
--data '{
    "itemId": 1,
    "name": "Monitor",
    "quantity": 1,
    "price": 14000.50
}'
```

DELETE request for deleting items in cart
```declarative

```

update request for updating items in cart
```declarative

```

