package main

import (
	"log"
	"os"

	"github.com/zzh8829/tron-server-go/cmd/tron-server-go/app"
)

func main() {
	rootCmd := app.NewRootCommand()
	if err := rootCmd.Execute(); err != nil {
		log.Printf("cmd error\n")
		os.Exit(1)
	}
}
