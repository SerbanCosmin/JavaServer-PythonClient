import socket

def save_image(data, filename):
    """Save binary data as image file."""
    with open(filename, 'wb') as file:
        file.write(data)

def main():
    server_ip = '127.0.0.1'
    server_port = 12345

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client_socket:
        client_socket.connect((server_ip, server_port))
        print("Connected to the server.")

        while True:
            message = input("Enter emoji command ':)' or ':(' or 'exit' to quit: ")
            if message.lower() == 'exit':
                break

            client_socket.sendall((message + "\n").encode()) #message with newline after

            # Response of server
            response = client_socket.recv(1024).decode()  # buffer
            if response.startswith("ERROR"):
                print(response.strip())  # error case
                continue
            elif response.startswith("OK"):
                print("Receiving image...")
                image_data = b''

                # read image
                while True:
                    chunk = client_socket.recv(4096)
                    if not chunk:  # close connection
                        break
                    image_data += chunk

                # save image
                filename = f"{message.replace(':', '')}.png"
                save_image(image_data, filename)
                print(f"Emoji saved as {filename}")
            else:
                print("Unexpected response from server.")

        print("Disconnected from the server.")

if __name__ == "__main__":
    main()
