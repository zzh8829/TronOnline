package app

import (
	"log"

	"github.com/spf13/cobra"
	"github.com/zzh8829/tron-server-go/pkg/server"
)

// NewRootCommand new
func NewRootCommand() *cobra.Command {
	var rootCmd = &cobra.Command{
		Use:   "tron-server-go",
		Short: "Tron Online Server",
		RunE:  runServer,
	}
	return rootCmd
}

func runServer(cmd *cobra.Command, args []string) error {
	log.Printf("Server Starting\n")

	s, err := server.NewServer()
	if err != nil {
		log.Printf("new server error\n")
		return err
	}

	if err := s.Run(); err != nil {
		log.Print("run server error\n")
		return err
	}

	return nil
}
