package server

import "errors"

// var accounts = make(map[string]string)
var accounts = map[string]string{
	"test":  "test",
	"test2": "test2",
	"test3": "test3",
}

// Register fn
func Register(username string, password string) error {
	_, exists := accounts[username]
	if exists {
		return errors.New("username already exists")
	}

	accounts[username] = password

	return nil
}

// Login fn
func Login(username string, password string) error {
	pw, exists := accounts[username]

	if !exists || password != pw {
		return errors.New("wrong password")
	}

	return nil
}
