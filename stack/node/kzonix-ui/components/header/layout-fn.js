import Header from './header'

const layoutStyle = {
  margin: '20px',
  padding: '20px',
  border: '1px solid #DDD'
}

const withHeader = Page => () => (
  <div style={layoutStyle}>
    <Header />
    <Page />
  </div>
)

export default withHeader
