@objc(simColourBlindPlugin)
public class simColourBlindPlugin: NSObject, FrameProcessorPluginBase {
  @objc public static func callback(_ frame: Frame!, withArgs _: [Any]!) -> Any! {
    guard let imageBuffer = CMSampleBufferGetImageBuffer(frame.buffer) else {
            return nil
      }
    // code goes here
    CVPixelBufferLockBaseAddress(imageBuffer, CVPixelBufferLockFlags(rawValue: 0))

        let bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer)
        let height = CVPixelBufferGetHeight(imageBuffer)
        guard let src_buff = CVPixelBufferGetBaseAddress(imageBuffer) else {
          return nil
        }

        let data = Data(bytes: src_buff, count: bytesPerRow * height)
        return data.base64EncodedString()
  }
}