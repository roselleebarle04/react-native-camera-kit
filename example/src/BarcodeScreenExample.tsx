import React, {Component} from 'react';
import CameraScreen from '../../src/CameraScreen';
import CheckingScreen from './CheckingScreen';

export default class BarcodeScreenExample extends Component {
  constructor(props) {
    super(props);
    this.state = {
      example: undefined,
      value: undefined,
    };
  }

  render() {
    if (this.state.example) {
      const Screen = this.state.example;
      return <Screen value={this.state.value}/>;
    }
    return (
        <CameraScreen
            onHistory={() => {
            }}
            onGallery={() => {
            }}
            flashImages={{
              // optional, images for flash state
              on: require('./assets/flash.png'),
              off: require('./assets/flash-off.png'),
              auto: require('./assets/auto-flash.png'),
            }}
            cameraFlipImage={require('./assets/switch-camera.png')} // optional, image for flipping camera button
            torchOnImage={require('./assets/idea.png')} // optional, image for toggling on flash light
            torchOffImage={require('./assets/idea.png')} // optional, image for toggling off flash light
            hideControls={false} // (default false) optional, hides camera controls
            showCapturedImageCount={false} // (default false) optional, show count for photos taken during that capture session
            scanBarcode
            onReadCode={(event) => {
              this.setState({example: CheckingScreen, value: event.nativeEvent.codeStringValue});
            }} // optional
            showFrame // (default false) optional, show frame with transparent layer (qr code or barcode will be read on this area ONLY), start animation for scanner,that stoped when find any code. Frame always at center of the screen
            laserColor="white" // (default red) optional, color of laser in scanner frame
            frameColor="#2078b6" // (default white) optional, color of border of scanner frame
        />
    );
  }
}
